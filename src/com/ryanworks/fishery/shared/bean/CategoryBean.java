package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class CategoryBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String name;
    private String description;
    
    public CategoryBean() 
    {
        this.id = "CAT-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getName() { return getSafeString(this.name); }
    public String getDescription() { return getSafeString(this.description); }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setName(String val) 
    {
        this.name = val;
    }
    
    public void setDescription(String val)
    {
        this.description = val;
    }
    
    @Override
    public String toString() 
    {
        return this.name;
    }
}