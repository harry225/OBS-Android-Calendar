package com.obs.mobile_tablet.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevenguitar on 1/14/14.
 */
public class BadRequestException extends IOException {
    Map<String, Object> properties;

    public BadRequestException() {
        properties = new HashMap<String, Object>();
    }

    public void addEntry(String key, Object value){
        properties.put(key, value);
    }

    public Object getEntry(String key){
        return properties.get(key);
    }
}
