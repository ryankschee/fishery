package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;
import java.util.UUID;

public class CustomerTransactionBean 
    extends BeanClass
    implements Comparable<CustomerTransactionBean> {
    
    public final static int TYPE_SALES = 10;
    public final static int TYPE_PAYMENT = 20;
    
    private String id;
    private long date;
    private String description;
    private double credit;
    private double debit;
    private double total;
    private int type;
    private String checkResult;
    
    public CustomerTransactionBean()
    {
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() { return getSafeString(this.id); }
    public long getDate() { return this.date; }
    public String getDescription() { return getSafeString(this.description); }
    public double getCredit() { return this.credit; }
    public double getDebit() { return this.debit; }
    public double getTotal() { return this.total; }
    public int getType() { return this.type; }
    public String getCheckResult() { return this.checkResult; }
    
    public void setDate(long val)
    {
        this.date = val;
    }
    
    public void setDescription(String val)
    {
        this.description = val;
    }
    
    public void setCredit(double val)
    {
        this.credit = val;
    }
    
    public void setDebit(double val)
    {
        this.debit = val;
    }

    public void setTotal(double val)
    {
        this.total = val;
    }
    
    public void setType(int val) 
    {
        this.type = val;
    }
    
    public void setCheckResult(String val)
    {
        this.checkResult = val;
    }
    
    @Override
    public int compareTo(CustomerTransactionBean bean) 
    {
        Calendar thisTime = Calendar.getInstance();
        thisTime.setTimeInMillis( this.getDate() );
        
        Calendar thatTime = Calendar.getInstance();
        thatTime.setTimeInMillis( bean.getDate() );
        
        return thisTime.compareTo(thatTime);
    }
}
