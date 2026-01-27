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