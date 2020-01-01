package com.ryanworks.fishery.shared.bean;

public class BeanClass {
    protected String getSafeString(String val) {
        if (val==null)
            return "";
        return val;
    }
}
