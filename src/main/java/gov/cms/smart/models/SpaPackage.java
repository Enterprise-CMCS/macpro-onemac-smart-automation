package gov.cms.smart.models;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class SpaPackage extends BasePackage {

    @Override
    public void generatePackageId() { /* IDs come from Excel */ }

}
