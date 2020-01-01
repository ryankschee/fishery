package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class SupplierCashBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String supplierId;
    private long cashDate;
    private String cashDesc;
    private double cashAmount;
    
    public SupplierCashBean() 
    {
        this.id = "SCASH-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public long getCashDate() { return this.cashDate; }
    public String getCashDesc() { return getSafeString(this.cashDesc); }
    public double getCashAmount() { return this.cashAmount; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setSupplierId(String val) 
    {
        this.supplierId = val;
    }
    
    public void setCashDate(long val)
    {
        this.cashDate = val;
    }
    
    public void setCashDesc(String val)
    {
        this.cashDesc = val;
    }
    
    public void setCashAmount(double val)
    {
        this.cashAmount = val;
    }
}