package gov.cms.smart.utils.accessibility;

import com.deque.html.axecore.results.Node;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessibilityUtils {

    public Results analyzePage(WebDriver driver) {
        try {
            return new AxeBuilder().analyze(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to run axe accessibility scan.", e);
        }
    }

    public Results analyzeSection(WebDriver driver, String cssSelector) {
        try {
            return new AxeBuilder()
                    .include(cssSelector)
                    .analyze(driver);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to run axe accessibility scan for selector: " + cssSelector, e
            );
        }
    }

    public List<Rule> getAllViolations(Results results) {
        if (results == null || results.getViolations() == null) {
            return Collections.emptyList();
        }
        return results.getViolations();
    }

    public List<Rule> getIgnoredViolations(Results results) {
        List<Rule> ignored = new ArrayList<>();
        for (Rule violation : getAllViolations(results)) {
            if (isIgnoredViolation(violation)) {
                ignored.add(violation);
            }
        }
        return ignored;
    }

    public List<Rule> getActionableViolations(Results results) {
        List<Rule> actionable = new ArrayList<>();
        for (Rule violation : getAllViolations(results)) {
            if (!isIgnoredViolation(violation)) {
                actionable.add(violation);
            }
        }
        return actionable;
    }

    public boolean hasNoActionableViolations(Results results) {
        return getActionableViolations(results).isEmpty();
    }

    private boolean isIgnoredViolation(Rule violation) {
        if (violation == null || violation.getId() == null) {
            return false;
        }

        String ruleId = violation.getId();

        if ("meta-viewport-large".equals(ruleId)) {
            return true;
        }

        if ("region".equals(ruleId) && isSalesforceShellViolation(violation)) {
            return true;
        }

        if ("aria-required-children".equals(ruleId) && isSalesforceTablistViolation(violation)) {
            return true;
        }

        if ("listitem".equals(ruleId) && isIframeListItemViolation(violation)) {
            return true;
        }

        if ("aria-prohibited-attr".equals(ruleId) && isDashboardIframeAriaViolation(violation)) {
            return true;
        }
        if ("aria-allowed-role".equals(ruleId) && isDashboardIframeAriaAllowedRoleViolation(violation)) {
            return true;
        }
        if ("list".equals(ruleId) && isDashboardIframeListViolation(violation)) {
            return true;
        }
        return false;
    }
    private boolean isDashboardIframeAriaAllowedRoleViolation(Rule violation) {
        if (violation.getNodes() == null || violation.getNodes().isEmpty()) {
            return false;
        }

        return violation.getNodes().stream().allMatch(this::isDashboardIframeAriaAllowedRoleNode);
    }
    private boolean isDashboardIframeListViolation(Rule violation) {
        if (violation.getNodes() == null || violation.getNodes().isEmpty()) {
            return false;
        }

        return violation.getNodes().stream().allMatch(this::isDashboardIframeListNode);
    }

    private boolean isDashboardIframeListNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase().trim();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return target.contains("iframe[name=\"sfxdash-")
                && html.startsWith("<ol");
    }
    private boolean isDashboardIframeAriaAllowedRoleNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return target.contains("iframe[name=\"sfxdash-")
                && html.contains("<li")
                && html.contains("role=\"button\"");
    }
    private boolean isSalesforceShellViolation(Rule violation) {
        if (violation.getNodes() == null || violation.getNodes().isEmpty()) {
            return false;
        }

        return violation.getNodes().stream().allMatch(this::isSalesforceShellNode);
    }

    private boolean isSalesforceShellNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return html.contains("forceskiplink")
                || html.contains("data-aura")
                || html.contains("one-appnav")
                || html.contains("appname")
                || html.contains("slds-context-bar")
                || target.contains("one-appnav")
                || target.contains("appname");
    }

    private boolean isSalesforceTablistViolation(Rule violation) {
        if (violation.getNodes() == null || violation.getNodes().isEmpty()) {
            return false;
        }

        return violation.getNodes().stream().allMatch(this::isSalesforceTablistNode);
    }

    private boolean isSalesforceTablistNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return html.contains("role=\"tablist\"")
                && (html.contains("uitabbar")
                || target.contains("uitabbar")
                || target.contains("one-record-home-flexipage")
                || target.contains("forcegenerated-flexipage"));
    }

    private boolean isIframeListItemViolation(Rule violation) {
        if (violation.getNodes() == null || violation.getNodes().isEmpty()) {
            return false;
        }

        return violation.getNodes().stream().allMatch(this::isIframeListItemNode);
    }

    private boolean isIframeListItemNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase().trim();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return "<li>".equals(html) && target.contains("iframe");
    }



    private boolean isDashboardIframeListItemNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase().trim();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return "<li>".equals(html)
                && target.contains(".dashboardcontainer > iframe");
    }

    private boolean isDashboardIframeAriaViolation(Rule violation) {
        if (violation.getNodes() == null || violation.getNodes().isEmpty()) {
            return false;
        }

        return violation.getNodes().stream().allMatch(this::isDashboardIframeAriaNode);
    }

    private boolean isDashboardIframeAriaNode(Node node) {
        String html = safe(node.getHtml()).toLowerCase();
        String target = node.getTarget() == null
                ? ""
                : node.getTarget().toString().toLowerCase();

        return target.contains("iframe")
                && html.contains("id=\"assistive-text-announcement\"")
                && html.contains("dashboard refreshed");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    public String buildAllViolationMessage(Results results) {
        return buildMessage(getAllViolations(results), "All Accessibility Violations");
    }

    public String buildIgnoredViolationMessage(Results results) {
        return buildMessage(getIgnoredViolations(results), "Ignored Accessibility Violations");
    }

    public String buildActionableViolationMessage(Results results) {
        return buildMessage(getActionableViolations(results), "Actionable Accessibility Violations");
    }

    private String buildMessage(List<Rule> violations, String title) {
        if (violations == null || violations.isEmpty()) {
            return title + ": 0";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(title).append(": ").append(violations.size()).append("\n\n");

        for (Rule violation : violations) {
            sb.append("Rule: ").append(violation.getId()).append("\n");
            sb.append("Impact: ").append(violation.getImpact()).append("\n");
            sb.append("Description: ").append(violation.getDescription()).append("\n");
            sb.append("Help: ").append(violation.getHelp()).append("\n");
            sb.append("Nodes: ")
                    .append(violation.getNodes() == null ? 0 : violation.getNodes().size())
                    .append("\n");

            if (violation.getNodes() != null) {
                for (Node node : violation.getNodes()) {
                    sb.append("  - Target: ").append(node.getTarget()).append("\n");
                    sb.append("    HTML: ").append(node.getHtml()).append("\n");
                }
            }

            sb.append("--------------------------------------------------\n");
        }

        return sb.toString();
    }
}