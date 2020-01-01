package com.ryanworks.fishery.shared.bean;

public class SystemBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private int inTransactionId;
    private int salesId;
    
    public SystemBean() 
    {}
    
    public boolean isValid() 
    {
        return true;
    }
    
    public String getId() { return getSafeString(this.id); }
    public int getInTransactionId() { return this.inTransactionId; }
    public int getSalesId() { return this.salesId; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setInTransactionId(int val) 
    {
        this.inTransactionId = val;
    }
    
    public void setSalesId(int val)
    {
        this.salesId = val;
    }    
}