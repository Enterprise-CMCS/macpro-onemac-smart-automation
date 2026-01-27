package gov.cms.smart.models;

public class WaiverPackage extends BasePackage {

    // Only waivers have action types & parents
    private String actionType; // Initial | Renewal | Amendment | Temporary Extension
    private String parentId;   // required for Renewal/Amendment/TE

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public void generatePackageId() { /* IDs come from Excel */ }
}
