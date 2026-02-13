# OneMAC SMART Automation Framework

This repository contains the **UI Automation Framework for the OneMAC SMART application**, built using **Selenium WebDriver,
TestNG,** and **Page Object Model (POM).**
The framework is **data-driven**, supports dynamic package ID generation, maintains **Excel-based test data pools**, and
integrates with **GitHub Actions CI/CD.**

## 1. Framework Overview

The OneMAC SMART Automation Framework automates SPA and Waiver workflows for the OneMAC SMART System.
It is built for:

* Cross-browser UI testing

* Dynamic package number generation using:

    * `state_counters.xlsx`

    * `packages.xlsx`

* **Data-driven testing** using Excel-backed selectors

* **Reusable page objects**

* **Rich reporting** using ExtentReports

* **Scheduled and on-push GitHub Actions pipelines**

The framework is structured by purpose, not UI layout - every package contains code responsible for a single job:

* `pages/` → UI interaction

* `models/` → Data objects for SPA/Waiver

* `utils/` → Excel trackers, ID generators, driver utilities

* `tests/` → Actual test scenarios

## 2. Project Structure

```text
smart-test-automation/
├── .github/
│   └── workflows/               # GitHub Actions CI pipelines
│
├── extent-report/               # Extent HTML reports + screenshots
├── logs/                        # Test execution logs
│
├── src/
│   ├── main/java/gov/cms/smart/
│   │   ├── pages/               # Page Object Model (POM)
│   │   │   ├── LoginPage.java
│   │   │   ├── NewSPAPage.java
│   │   │   ├── NewWaiverPage.java
│   │   │   ├── SpaDetailsPage...
│   │   │
│   │   ├── models/              # SPA & Waiver POJOs
│   │   │   ├── BasePackage.java
│   │   │   ├── SpaPackage.java
│   │   │   └── WaiverPackage.java
│   │   │
│   │   └── utils/               # Framework utilities
│   │       ├── DriverFactory.java
│   │       ├── ConfigReader.java
│   │       ├── PageFactory.java
│   │       ├── UIElementUtils.java
│   │       ├── SpaIdGenerator.java
│   │       ├── WaiverIdGenerator.java
│   │       ├── StateCounterTracker.java
│   │       ├── ExcelPackageTracker.java
│   │       ├── ExcelPackageSelector.java
│   │       └── ScreenshotUtil.java
│   │
│   ├── test/java/gov/cms/smart/
│   │   ├── base/
│   │   │   └── BaseTest.java
│   │   ├── tests/
│   │   │   └── MedicaidSPATests.java
│   │   │
│   │   └── utils/
│   │      └── TestListener.java
│   │  
│   │
│   └── resources/
│       ├── config.properties
│       ├── log4j2.xml
│       ├── packages.xlsx               # Generated test data (SPA + Waivers)
│       └── state_counters.xlsx         # Controlled increment for each state
│
├── pom.xml
└── README.md


```
## 3. Package ID Management (SPA, Waivers, TE, Amendments)

The framework generates realistic, CMS-compliant IDs using:

`state_counters.xlsx`

Tracks last used numbers per state:

```
State | SPA_Last | Waiver_Last | Renewal_Last | Amendment_Last | TE_Last
MD    | 9025     | 2212        | 1            | 3              | 2
```

`packages.xlsx`

Stores every SPA, Waiver, Amendment, and TE created during automation:

```
PackageType | State | Authority | ActionType | PackageID       | Status | ParentID
SPA         | MD    | Medicaid   | ""         | MD-25-9001      | ""     |
Waiver      | MD    | 1915(b)    | Initial    | MD-2200.R00.00  | ""     |
Waiver      | MD    | 1915(b)    | Renewal    | MD-2200.R01.00  | ""     | MD-2200.R00.00
Waiver      | MD    | 1915(b)    | Temp Ext   | MD-2200.R01.TE01| ""     | MD-2200.R01.00
```

**ID Generator Responsibilities**

| Class                    | Responsibility                                |
|--------------------------|-----------------------------------------------|
| **SpaIdGenerator**       | Generates next SPA ID using state counters    |
| **WaiverIdGenerator**    | Generates Initial, Renewal, Amendment, TE IDs |
| **ExcelPackageTracker**  | Writes new packages + updates status          |
| **ExcelPackageSelector** | Picks existing packages for new tests         |

## 4. Dynamic Package Pool (Auto-Generate Test Data)

The method below creates a fresh batch of SPA and Waiver test data:

```
PackagePoolGenerator.generate(List.of("MD", "CO", "AL", "NY"));
```

This populates:

* **6 SPAs per state**

* **6 Initial Waivers per state**

IDs are stored in:

`packages.xlsx`

`state_counters.xlsx`

## 5. Prerequisites

Before running the tests, make sure you have the following installed:

* **Java 17**
* **Maven**
* **Git**
* Chrome or Firefox (for local runs)

## 6. Configuration: Browser and Headless Mode

The browser and headless mode are configured in the `config.properties` file located at:

`src/test/resources/config.properties`

**Example `config.properties`:**

```properties
browser=chrome
headless=true
```

* browser can be chrome or firefox

* headless can be true or false

## 7. Clone Repository

Clone the repository to your local machine:

`git clone https://github.com/Enterprise-CMCS/macpro-onemac-smart-automation`

`cd macpro-onemac-smart-automation`

## 8. Running Tests Locally

Run tests using Maven:

`mvn clean test`

* The browser and headless settings are taken from config.properties.

* ExtentReports will be generated in extent-report/ only when running test suite from xml files located under resources directory.

* Screenshots of failures will be saved in extent-report/screenshots/.

## 9. GitHub Actions (CI)

* Workflow is defined in .github/workflows/onemac-smart-tests.yml.

* On push to main or manual trigger, GitHub Actions will:

    * Set up Java 17

    * Install Chrome (or Firefox if configured)

    * Run Maven tests

    * Upload ExtentReports and screenshots as artifacts

**Note:** For stability, Chrome is recommended on CI.

## 10. Reports

* ExtentReports are generated at: `extent-report/OneMAC-SMART-TestReport.html`

* Screenshots of failed tests: `extent-report/screenshots/`

* Reports are uploaded as artifacts in GitHub Actions for each workflow run.

## 11. Git Operations

**Adding Changes**

`git add .`

`git commit -m "Your commit message"`

`git push origin main`

* Ensure your Personal Access Token (PAT) is set up for GitHub authentication.

**Creating/Updating Workflows**

* `.github/workflows/onemac-smart-tests.yml` defines the CI workflow.

* Any changes to workflow files require a PAT with workflow scope.

## 12. Troubleshooting

* **SessionNotCreatedException / Marionette errors:**

    * Ensure the Firefox or Chrome driver version matches the browser version.

    * Headless mode arguments for CI:

```properties
--headless=new
--width=1920
--height=1080
--no-sandbox
--disable-dev-shm-usage
--disable-gpu

```

* **Browser installed incorrectly on CI:**

    * Firefox may fail on Linux runners; Chrome is recommended.

* **SLF4J warnings:**

    * These are logging warnings and can be ignored unless logging is required.

* **ExtentReports not uploading:**

    * Make sure `path` in `upload-artifact` action matches `extent-report/**` and `extent-report/screenshots/**`.

    * Ensure if: `always()` is used in workflow step to upload reports even on test failures.

* **Running a single browser:**

Use the browser specified in `config.properties` and pass it as a system property if needed:

mvn clean test -Dbrowser=firefox -Dheadless=true

## 13. Writing Tests (Example)

**Creating a SPA**
```properties
String spaId = SpaIdGenerator.nextSpa("MD", "25");

ExcelPackageTracker.appendNewPackage(
"SPA", "MD", "Medicaid SPA", "", spaId, "", ""
);
```
**Creating a Renewal**
```properties
WaiverPackage parent = ExcelPackageSelector.selectApprovedInitial("MD", "1915(b)");
String renewalId = WaiverIdGenerator.nextRenewal(parent.getPackageId(),
                     ExcelPackageTracker.readAllIds("MD"));

ExcelPackageTracker.appendNewPackage(
    "Waiver", parent.getState(), parent.getAuthority(),
    "Renewal", renewalId, "", parent.getPackageId()
);
```
**Selecting an Unapproved SPA**

```properties
SpaPackage spa = ExcelPackageSelector.selectRandomSPA("MD", "Medicaid SPA");
```

## 14. Additional Notes

* Keep `config.properties` updated for headless or browser preferences.

* CI workflow is currently optimized for Linux runners.

* Ensure GitHub Actions runner has network access to download browsers and WebDriver binaries.