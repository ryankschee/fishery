package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;
import java.util.UUID;

public class CustomerSummaryBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String customerId;
    private long firstDay;
    private int year;
    private int month;
    private double balance;
    private double totalSalesAmount;
    private double totalPaymentAmount;
    
    public CustomerSummaryBean() 
    {
        this.id = "CSM-" + UUID.randomUUID().toString();
    }
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String generateId() 
    {
        this.id = "CSM-" + UUID.randomUUID().toString();
        return this.id;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public long getFirstDay() { return this.firstDay; }
    public int getYear() { return this.year; }
    public int getMonth() { return this.month; }
    public double getBalance() { return this.balance; }
    public double getTotalSalesAmount() { return this.totalSalesAmount; }
    public double getTotalPaymentAmount() { return this.totalPaymentAmount; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setCustomerId(String val) 
    {
        this.customerId = val;
    }
    
    public void setFirstDay(long val)
    {
        this.firstDay = val;
    }
    
    public void setYear(int val)
    {
        this.year = val;
    }
    
    public void setMonth(int val)
    {
        this.month = val;
    }
    
    public void setBalance(double val)
    {
        this.balance = val;
    }
    
    public void setTotalSalesAmount(double val)
    {
        this.totalSalesAmount = val;
    }
    
    public void setTotalPaymentAmount(double val)
    {
        this.totalPaymentAmount = val;
    }
}