package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class SupplierMiscBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String supplierId;
    private long miscDate;
    private String miscDesc;
    private double miscAmount;
    
    public SupplierMiscBean() 
    {
        this.id = "SMI-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public long getMiscDate() { return this.miscDate; }
    public String getMiscDesc() { return getSafeString(this.miscDesc); }
    public double getMiscAmount() { return this.miscAmount; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setSupplierId(String val) 
    {
        this.supplierId = val;
    }
    
    public void setMiscDate(long val)
    {
        this.miscDate = val;
    }
    
    public void setMiscDesc(String val)
    {
        this.miscDesc = val;
    }
    
    public void setMiscAmount(double val)
    {
        this.miscAmount = val;
    }
}