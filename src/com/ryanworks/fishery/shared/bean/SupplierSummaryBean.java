package com.ryanworks.fishery.shared.bean;

import java.util.UUID;

public class SupplierSummaryBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String supplierId;
    private long firstDay;
    private long endDay;
    private double balance;
    private double totalCreditAmount;
    private double totalDebitAmount;
    
    public SupplierSummaryBean() 
    {
        this.id = "SSM-" + UUID.randomUUID().toString();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String generateId() 
    {
        this.id = "SSM-" + UUID.randomUUID().toString();
        return this.id;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public long getFirstDay() { return this.firstDay; }
    public long getEndDay() { return this.endDay; }
    public double getBalance() { return this.balance; }
    public double getTotalCreditAmount() { return this.totalCreditAmount; }
    public double getTotalDebitAmount() { return this.totalDebitAmount; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setSupplierId(String val) 
    {
        this.supplierId = val;
    }
    
    public void setFirstDay(long val)
    {
        this.firstDay = val;
    }
    
    public void setEndDay(long val)
    {
        this.endDay = val;
    }
        
    public void setBalance(double val)
    {
        this.balance = val;
    }
    
    public void setTotalCreditAmount(double val)
    {
        this.totalCreditAmount = val;
    }
    
    public void setTotalDebitAmount(double val)
    {
        this.totalDebitAmount = val;
    }
}