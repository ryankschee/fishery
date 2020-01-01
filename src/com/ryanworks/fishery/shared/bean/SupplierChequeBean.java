package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class SupplierChequeBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String supplierId;
    private long chequeDate;
    private String chequeNo;
    private double chequeAmount;
    
    public SupplierChequeBean() 
    {
        this.id = "SCHQ-" + Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public long getChequeDate() { return this.chequeDate; }
    public String getChequeNo() { return getSafeString(this.chequeNo); }
    public double getChequeAmount() { return this.chequeAmount; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setSupplierId(String val) 
    {
        this.supplierId = val;
    }
    
    public void setChequeDate(long val)
    {
        this.chequeDate = val;
    }
    
    public void setChequeNo(String val)
    {
        this.chequeNo = val;
    }
    
    public void setChequeAmount(double val)
    {
        this.chequeAmount = val;
    }
}