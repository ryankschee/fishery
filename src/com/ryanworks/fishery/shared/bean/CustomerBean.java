package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class CustomerBean 
    extends BeanClass
    implements java.io.Serializable, Comparable
{
    private String id;
    private String name;
    private String notes;
    private double balance;
    private long balanceLastUpdate;
    private boolean changed = false;
    
    public CustomerBean() 
    {
        this.id = "";
        this.name = "";
        this.balance = 0.0d;
        this.balanceLastUpdate = Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getName() { return getSafeString(this.name); }
    public String getNotes() { return getSafeString(this.notes); }
    public double getBalance() { return this.balance; }
    public long getBalanceLastUpdate() { return this.balanceLastUpdate; }
    public boolean isChanged() { return this.changed; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setName(String val) 
    {
        this.name = val;
    }
    
    public void setNotes(String val)
    {
        this.notes = val;
    }
    
    public void setBalance(double val)
    {
        this.balance = val;
    }
    
    public void setBalanceLastUpdate(long val)
    {
        this.balanceLastUpdate = val;
    }
    
    public void setChanged(boolean val)
    {
        this.changed = val;
    }
    
    @Override
    public String toString()
    {
        return this.getId() + " - " + this.getName();
    }

    @Override
    public int compareTo(Object o) {
        return this.id.compareTo( ((CustomerBean)o).getId());
    }
}