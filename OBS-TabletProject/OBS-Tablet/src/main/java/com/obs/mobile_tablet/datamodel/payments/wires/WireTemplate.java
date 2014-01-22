package com.obs.mobile_tablet.datamodel.payments.wires;

/**
 * Created by david on 1/17/14.
 */
public class WireTemplate {
    private String templateId;
    private String templateName;

    public WireTemplate() {
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
