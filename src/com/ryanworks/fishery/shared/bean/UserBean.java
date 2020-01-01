package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class UserBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String username;
    private String password;
    private long lastLogin;
    
    public UserBean() 
    {
        this.id = "USR-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getUsername() { return getSafeString(this.username); }
    public String getPassword() { return getSafeString(this.password); }
    public long getLastLogin() { return this.lastLogin; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setUsername(String val) 
    {
        this.username = val;
    }
    
    public void setPassword(String val)
    {
        this.password = val;
    }
    
    public void setLastLogin(long val)
    {
        this.lastLogin = val;
    }
}