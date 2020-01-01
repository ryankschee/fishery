package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class ItemBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String name;
    private String nameBm;
    private String newName; // not for database storage, just for report.
    private double price;
    private String description;
    private String photoUrl;
    private String categoryId;
    
    public ItemBean() 
    {
        this.id = "ITM-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getName() { return getSafeString(this.name); }
    public String getNameBm() { return getSafeString(this.nameBm); }
    public String getNewName() { return getSafeString(this.newName); }
    public double getPrice() { return this.price; }
    public String getDescription() { return getSafeString(this.description); }
    public String getPhotoUrl() { return getSafeString(this.photoUrl); }
    public String getCategoryId() { return getSafeString(this.categoryId); }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setName(String val) 
    {
        this.name = val;
    }
    
    public void setNameBm(String val)
    {
        this.nameBm = val;
    }
    
    public void setNewName(String val)
    {
        this.newName = val;
    }
    
    public void setPrice(double val)
    {
        this.price = val;
    }
    
    public void setDescription(String val)
    {
        this.description = val;
    }
    
    public void setPhotoUrl(String val)
    {
        this.photoUrl = val;
    }
    
    public void setCategoryId(String val) 
    {
        this.categoryId = val;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
}