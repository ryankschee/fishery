package com.ryanworks.fishery.shared.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SalesLineBean 
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
    private double savingPrice;
    private String bucketNo;
    private String supplierId;
    private String supplierName;
    private long dateTime;
    private String customerId;
    private String salesId;
    private double addWeight;
    
    // flag to indicate not to save this bean
    private boolean saveable = true; 
    // groupIdList
    public List<String> groupIdList = new ArrayList<String>();
    
    public SalesLineBean()
    {
        this.id = "SLI-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() { return true; }
    
    public boolean isSelected() { return this.selected; }
    
    public boolean isSaveable() { return this.saveable; }
    public String getId() { return getSafeString(this.id); }
    public String getItemId() { return getSafeString(this.itemId); }
    public String getItemName() { return getSafeString(this.itemName); }
    public String getItemNewName() { return getSafeString(this.itemNewName); }
    public String getItemNameBm() { return getSafeString(this.itemNameBm); }
    public double getWeight() { return this.weight; }
    public double getUnitPrice() { return this.unitPrice; }
    public double getSavingPrice() { return this.savingPrice; }
    public String getBucketNo() { return getSafeString(this.bucketNo); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public String getSupplierName() { return getSafeString(this.supplierName); }
    public long getDateTime() { return this.dateTime; }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public String getSalesId() { return getSafeString(this.salesId); }
    public double getAddWeight() { return this.addWeight; }
    
    public void setSaveable(boolean val)
    {
        this.saveable = val;
    }
    
    public void setSelected(boolean val)
    {
        this.selected = val;
    }
    
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
    
    public void setSavingPrice(double val)
    {
        this.savingPrice = val;
    }
    
    public void setBucketNo(String val) 
    {
        this.bucketNo = val;
    }
    
    public void setSupplierId(String val)
    {
        this.supplierId = val;
    }
    
    public void setSupplierName(String val)
    {
        this.supplierName = val;
    }
    
    public void setDateTime(long val)
    {
        this.dateTime = val;
    }
    
    public void setCustomerId(String val)
    {
        this.customerId = val;
    }
    
    public void setSalesId(String val)
    {
        this.salesId = val;
    }
    
    public void setAddWeight(double val)
    {
        this.addWeight = val;
    }
}
