package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class SupplierBean 
    extends BeanClass
    implements java.io.Serializable, Comparable
{
    private String id;
    private String shipNumber;
    private String name;
    private double percentage;
    private String notes;
    private boolean frequent;
    private boolean savingAccount;
    private boolean changed = false;
    
    public SupplierBean() 
    {
        this.id = "SUP-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getShipNumber() { return getSafeString(this.shipNumber); }
    public String getName() { return getSafeString(this.name); }
    public double getPercentage() { return this.percentage; }
    public String getNotes() { return getSafeString(this.notes); }
    public boolean isFrequent() { return this.frequent; }
    public boolean isSavingAccount() { return this.savingAccount; }
    public boolean isChanged() { return this.changed; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setShipNumber(String val) 
    {
        this.shipNumber = val;
    }
    
    public void setName(String val) 
    {
        this.name = val;
    }
    
    public void setPercentage(double val)
    {
        this.percentage = val;
    }
    
    public void setNotes(String val)
    {
        this.notes = val;
    }
    
    public void setFrequent(boolean val)
    {
        this.frequent = val;
    }
        
    public void setSavingAccount(boolean val)
    {
        this.savingAccount = val;
    }
    
    public void setChanged(boolean val)
    {
        this.changed = val;
    }
    
    @Override
    public String toString()
    {
        return "[" + id + "] " + name;
    }

    @Override
    public int compareTo(Object o) {
        return id.compareTo(((SupplierBean)o).getId());
    }
}