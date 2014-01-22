package com.obs.mobile_tablet.datamodel.reporting;

import java.util.List;
import java.util.Map;

public class AccountTypeObj {

    private String id;
    private String name;
    private List<Map<String, String>> thumbnailTypeCodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getThumbnailTypeCodes() {
        return thumbnailTypeCodes;
    }

    public void setThumbnailTypeCodes(List<Map<String, String>> thumbnailTypeCodes) {
        this.thumbnailTypeCodes = thumbnailTypeCodes;
    }
}
