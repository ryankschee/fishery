package com.ryanworks.fishery.shared.bean;

import java.util.UUID;

public class CustomerSalesCheckBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private String customerIdName;
    private CustomerBean customer;
    private SalesBean sales;
    private boolean printed = false;
    private boolean priceInvalid = false;
    private String errorMessage = "";
    
    public CustomerSalesCheckBean() 
    {
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getCustomerIdName() { return this.customerIdName; }
    public CustomerBean getCustomer() { return this.customer; }
    public SalesBean getSales() { return this.sales; }
    public boolean isPrinted() { return this.printed; }
    public boolean isPriceInvalid() { return this.priceInvalid; }
    public String getErrorMessage() { return this.errorMessage; }
    
    public void setCustomerIdName(String val) 
    {
        this.customerIdName = val;
    }
    
    public void setCustomer(CustomerBean val)
    {
        this.customer = val;
    }
    
    public void setSales(SalesBean val) 
    {
        this.sales = val;
    }
    
    public void setPrinted(boolean val)
    {
        this.printed = val;
    }
    
    public void setPriceInvalid(boolean val)
    {
        this.priceInvalid = val;
    }
    
    public void setErrorMessage(String val)
    {
        this.errorMessage = val;
    }
}