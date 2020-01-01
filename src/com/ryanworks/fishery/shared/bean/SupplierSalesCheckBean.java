package com.ryanworks.fishery.shared.bean;

import java.util.UUID;

public class SupplierSalesCheckBean 
    extends BeanClass
    implements java.io.Serializable 
{
    private String id;
    private SupplierBean supplier;
    private InTransactionBean sales;
    private boolean printed;
    private boolean priceInvalid;
    
    public SupplierSalesCheckBean() 
    {
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() { return getSafeString(this.id); }
    public SupplierBean getSupplier() { return this.supplier; }
    public InTransactionBean getSales() { return this.sales; }
    public boolean isPrinted() { return this.printed; }
    public boolean isPriceInvalid() { return this.priceInvalid; }
    
    public void setSupplier(SupplierBean val)
    {
        this.supplier = val;
    }
    
    public void setSales(InTransactionBean val) 
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
}