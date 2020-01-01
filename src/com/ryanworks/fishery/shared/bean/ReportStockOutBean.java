package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class ReportStockOutBean 
    extends BeanClass
    implements java.io.Serializable, Comparable<ReportStockOutBean>
{
    public static int seqno = 0;
    
    private String id;
    private String itemName;
    private String itemNewName;
    private double weight;
    private String customerId;
    private String bucketNo;
    
    public ReportStockOutBean()
    {
        this.id = "RSO" + Calendar.getInstance().getTimeInMillis() + "-" + (seqno++);
    }
    
    public boolean isValid() { return true; }
    
    public String getId() { return getSafeString(this.id); }
    public String getItemName() { return getSafeString(this.itemName); }
    public String getItemNewName() { return getSafeString(this.itemNewName); }
    public double getWeight() { return this.weight; }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public String getBucketNo() { return getSafeString(this.bucketNo); }
            
    public void setItemName(String val)
    {
        this.itemName = val;
    }
    
    public void setItemNewName(String val)
    {
        this.itemNewName = val;
    }
    
    public void setWeight(double val)
    {
        this.weight = val;
    }
    
    public void setCustomerId(String val)
    {
        this.customerId = val;
    }
    
    public void setBucketNo(String val) 
    {
        this.bucketNo = val;
    }    
    
    @Override
    public int compareTo(ReportStockOutBean bean) {
        return this.getItemNewName().compareTo( bean.getItemNewName() );
    }
}
