package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class SalesBucketBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String bucketNo;
    private long dateTime;
    private String customerId;
    private String salesId;
    
    public SalesBucketBean()
    {
        this.id = "BUC-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() { return true; }
    
    public String getId() { return getSafeString(this.id); }
    public String getBucketNo() { return getSafeString(this.bucketNo); }
    public long getDateTime() { return this.dateTime; }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public String getSalesId() { return getSafeString(this.salesId); }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setBucketNo(String val) 
    {
        this.bucketNo = val;
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
    
    public String toString()
    {
        return this.bucketNo;
    }
}
