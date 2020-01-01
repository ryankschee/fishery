package com.ryanworks.fishery.shared.bean;

import java.util.Date;

public final class LogBean
    extends BeanClass
    implements java.io.Serializable {

    private String id;
    private long dateTime;
    private String type;
    private String description;
    private String userId;

    public LogBean() {}
    
    public LogBean(String userId, String type, String logDesc) {

    	Date date = new Date();

        this.setId("LOG-"+date.getTime());
        this.setDateTime(date.getTime());
        this.setType(type);
        this.setDescription(logDesc);
        this.setId(userId);
    }

    public String getId() { return getSafeString(this.id); }
    public long getDateTime() { return this.dateTime; }
    public String getType() { return getSafeString(this.type); }
    public String getDescription() { return getSafeString(this.description); }
    public String getUserId() { return getSafeString(this.userId); }

    public boolean isValid() {
        return true;
    }

    public void setId(String val) {
        this.id = val;
    }
    
    public void setDateTime(long timeInMillis) {
        this.dateTime = timeInMillis;
    }

    public void setType(String val) {
        this.type = val;
    }
    
    public void setDescription(String val) {
        this.description = val;
    }
    
    public void setUserId(String val) {
        this.userId = val;
    }

}
