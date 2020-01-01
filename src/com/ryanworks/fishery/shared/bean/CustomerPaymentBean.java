package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class CustomerPaymentBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String customerId;
    private long date;
    private double amount;
    private String term;
    private String remarks;
    
    public CustomerPaymentBean() 
    {
        this.id = "PYT-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public long getDate() { return this.date; }
    public double getAmount() { return this.amount; }
    public String getTerm() { return getSafeString(this.term); }
    public String getRemarks() { return getSafeString(this.remarks); }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setCustomerId(String val) 
    {
        this.customerId = val;
    }
    
    public void setDate(long val)
    {
        this.date = val;
    }
    
    public void setAmount(double val)
    {
        this.amount = val;
    }
    
    public void setTerm(String val)
    {
        this.term = val;
    }
    
    public void setRemarks(String val)
    {
        this.remarks = val;
    }
}