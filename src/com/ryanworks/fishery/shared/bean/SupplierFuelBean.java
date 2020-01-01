package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class SupplierFuelBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String supplierId;
    private long fuelDate;
    private double fuelQuantity;
    private double fuelUnitPrice;
    private double fuelTotalPrice;
    
    public SupplierFuelBean() 
    {
        this.id = "SFU-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public long getFuelDate() { return this.fuelDate; }
    public double getFuelQuantity() { return this.fuelQuantity; }
    public double getFuelUnitPrice() { return this.fuelUnitPrice; }
    public double getFuelTotalPrice() { return this.fuelTotalPrice; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setSupplierId(String val) 
    {
        this.supplierId = val;
    }
    
    public void setFuelDate(long val)
    {
        this.fuelDate = val;
    }
    
    public void setFuelQuantity(double val)
    {
        this.fuelQuantity = val;
    }
    
    public void setFuelUnitPrice(double val)
    {
        this.fuelUnitPrice = val;
    }
    
    public void setFuelTotalPrice(double val)
    {
        this.fuelTotalPrice = val;
    }
}