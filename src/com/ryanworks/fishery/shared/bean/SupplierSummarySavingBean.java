package com.ryanworks.fishery.shared.bean;

import java.util.UUID;

public class SupplierSummarySavingBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String supplierId;
    private long firstDay;
    private long endDay;
    private double balance;
    private double totalSavingAmount;
    private double totalWithdrawalAmount;
    
    public SupplierSummarySavingBean() 
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
    public double getTotalSavingAmount() { return this.totalSavingAmount; }
    public double getTotalWithdrawalAmount() { return this.totalWithdrawalAmount; }
    
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
    
    public void setTotalSavingAmount(double val)
    {
        this.totalSavingAmount = val;
    }
    
    public void setTotalWithdrawalAmount(double val)
    {
        this.totalWithdrawalAmount = val;
    }
}