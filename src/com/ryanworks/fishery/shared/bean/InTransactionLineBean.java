package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class InTransactionLineBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private boolean selected = false;
    private String id;
    private String itemId;
    private String itemName;
    private String itemNewName;
    private String itemNameBm;
    private double weight;
    private double unitPrice;
    private String bucketNo;
    private String customerId;
    private String customerName;
    private String inTransactionId;
    private long dateTime;
    private String supplierId;
    private double saving;
    private String salesId;
    private String salesLineId;
        
    public InTransactionLineBean()
    {
        this.id = "ITL-" + Calendar.getInstance().getTimeInMillis();
        this.selected = false;
    }
    
    public boolean isValid() { return true; }
    
    public boolean isSelected() { return this.selected; }
    
    public String getId() { return getSafeString(this.id); }
    public String getItemId() { return getSafeString(this.itemId); }
    public String getItemName() { return getSafeString(this.itemName); }
    public String getItemNewName() { return getSafeString(this.itemNewName); }
    public String getItemNameBm() { return getSafeString(this.itemNameBm); }
    public double getWeight() { return this.weight; }
    public double getUnitPrice() { return this.unitPrice; }
    public String getBucketNo() { return getSafeString(this.bucketNo); }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public String getCustomerName() { return getSafeString(this.customerName); }
    public String getInTransactionId() { return getSafeString(this.inTransactionId); }
    public long getDateTime() { return this.dateTime; }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public double getSaving() { return this.saving; }
    public String getSalesId() { return getSafeString(this.salesId); }
    public String getSalesLineId() { return getSafeString(this.salesLineId); }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setItemId(String val) 
    {
        this.itemId = val;
    }
    
    public void setItemName(String val)
    {
        this.itemName = val;
    }
    
    public void setItemNewName(String val)
    {
        this.itemNewName = val;
    }
    
    public void setItemNameBm(String val)
    {
        this.itemNameBm = val;
    }
    
    public void setWeight(double val)
    {
        this.weight = val;
    }
    
    public void setUnitPrice(double val)
    {
        this.unitPrice = val;
    }
    
    public void setBucketNo(String val) 
    {
        this.bucketNo = val;
    }
    
    public void setCustomerId(String val)
    {
        this.customerId = val;
    }
    
    public void setCustomerName(String val)
    {
        this.customerName = val;
    }
    
    public void setInTransactionId(String val)
    {
        this.inTransactionId = val;
    }
    
    public void setDateTime(long val)
    {
        this.dateTime = val;
    }
    
    public void setSupplierId(String val)
    {
        this.supplierId = val;
    }
    
    public void setSaving(double val)
    {
        this.saving = val;
    }
    
    public void setSalesId(String val)
    {
        this.salesId = val;
    }
    
    public void setSalesLineId(String val)
    {
        this.salesLineId = val;
    }
    
    public void setSelected(boolean val)
    {
        this.selected = val;
    }
}
