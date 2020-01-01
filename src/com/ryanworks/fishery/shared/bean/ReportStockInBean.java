package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class ReportStockInBean 
    extends BeanClass
    implements java.io.Serializable, Comparable<ReportStockInBean>
{
    public static int seqno = 0;
    
    private String id;
    private String itemName;
    private String itemNewName;
    private String supplierId;
    private String invoiceNo;
    private double weight;
    private String customerId;
    private String bucketNo;
    
    public ReportStockInBean()
    {
        this.id = "RSI" + Calendar.getInstance().getTimeInMillis() + "-" + (seqno++);
    }
    
    public boolean isValid() { return true; }
    
    public String getId() { return getSafeString(this.id); }
    public String getItemName() { return getSafeString(this.itemName); }
    public String getItemNewName() { return getSafeString(this.itemNewName); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public String getInvoiceNo() { return getSafeString(this.invoiceNo); }
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
    
    public void setSupplierId(String val)
    {
        this.supplierId = val;
    }
    
    public void setInvoiceNo(String val)
    {
        this.invoiceNo = val;
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
    public int compareTo(ReportStockInBean bean) {
        return this.getItemNewName().compareTo( bean.getItemNewName() );
    }
}
