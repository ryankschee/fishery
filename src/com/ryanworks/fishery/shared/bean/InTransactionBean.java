package com.ryanworks.fishery.shared.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InTransactionBean 
    extends BeanClass
    implements java.io.Serializable, Comparable<InTransactionBean>
{
    private String id;
    private String supplierId;
    private String supplierName;
    private int supplierTrip;
    private long dateTime;
    private String transactionNo;
    private double totalPrice;
    private double totalBonus;
    private double totalSaving;
    private List<InTransactionLineBean> lineList;
    
    public InTransactionBean()
    {
        this.id = "ITR-" + Calendar.getInstance().getTimeInMillis();
        this.transactionNo = "";
    }
    
    public boolean isValid() { return true; }
    
    public String getId() { return getSafeString(this.id); }
    public String getSupplierId() { return getSafeString(this.supplierId); }
    public String getSupplierName() { return getSafeString(this.supplierName); }
    public int getSupplierTrip() { return this.supplierTrip; }
    public long getDateTime() { return this.dateTime; }
    public String getTransactionNo() { return getSafeString(this.transactionNo); }
    public double getTotalPrice() { return this.totalPrice; }
    public double getTotalBonus() { return this.totalBonus; }
    public double getTotalSaving() { return this.totalSaving; }
    
    public List<InTransactionLineBean> getLineList() 
    {
        if (this.lineList == null)
            this.lineList = new ArrayList<>();
        
        return this.lineList; 
    }
    
    public void addLineBean(InTransactionLineBean val)
    {
        this.getLineList().add(val);
    }
    
    public void removeLineBean(InTransactionLineBean val)
    {
        this.getLineList().remove(val);
    }
    
    public void setLineList(List<InTransactionLineBean> val)
    {
        this.lineList = val;
    }
            
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setSupplierId(String val) 
    {
        this.supplierId = val;
    }
    
    public void setSupplierName(String val)
    {
        this.supplierName = val;
    }
    
    public void setSupplierTrip(int val)
    {
        this.supplierTrip = val;
    }
    
    public void setDateTime(long val) 
    {
        this.dateTime = val;
    }
    
    public void setTransactionNo(String val)
    {
        this.transactionNo = val;
    }
    
    public void setTotalPrice(double val)
    {
        this.totalPrice = val;
    }
    
    public void setTotalBonus(double val)
    {
        this.totalBonus = val;
    }
    
    public void setTotalSaving(double val)
    {
        this.totalSaving = val;
    }
    
    @Override
    public int compareTo(InTransactionBean o) {
        int i = new Date(this.getDateTime()).compareTo( new Date(o.getDateTime()) );
        
        if (i != 0)
            return i;
                
        i = this.getSupplierId().compareTo(o.getSupplierId());
        if (i != 0)
            return i;
        
        return Integer.valueOf(this.getSupplierTrip()).compareTo(Integer.valueOf(o.getSupplierTrip()));
    }
}
