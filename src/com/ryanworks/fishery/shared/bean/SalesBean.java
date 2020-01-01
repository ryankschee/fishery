package com.ryanworks.fishery.shared.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SalesBean 
    extends BeanClass
    implements java.io.Serializable, Comparable<SalesBean>
{
    public final static int STATUS_COMPLETED = 0;
    public final static int STATUS_INCOMPLETE = 1;
    public final static String[] STATUS_MESSAGE = {"COMPLETED", "INCOMPLETE"};
    
    private String id;
    private String customerId;
    private String customerName;
    private int customerTrip;
    private long dateTime;
    private String invoiceNo;
    private double subTotalPrice;
    private double totalPrice;
    private int status = STATUS_INCOMPLETE;
    private int lineCount = 0;
    
    private List<SalesBucketBean> bucketList = new ArrayList<>();
    private List<SalesLineBean> lineList = new ArrayList<>();
    
    public SalesBean()
    {
        this.id = "SAL-" + Calendar.getInstance().getTimeInMillis();
        this.invoiceNo = "";
    }
    
    public boolean isValid() { return true; }
    
    public boolean hasInvoiceNo() {
        if (this.invoiceNo == null || "".equals(this.invoiceNo.trim()))
            return false;
        else
            return true;
    }
      
    public SalesBucketBean addBucket(String bucketNo)
    {
        return addBucket(bucketNo, Calendar.getInstance().getTimeInMillis());
    }
    
    public SalesBucketBean addBucket(String bucketNo, long dateTime)
    {
        // Check duplication before Add
        for (SalesBucketBean bucket : this.bucketList)
        {
            System.err.println("SalesBean.addBucket() - [1] adding bucket " + bucket.getBucketNo() + ", " + bucket.getDateTime());
            if (bucketNo.equalsIgnoreCase(bucket.getBucketNo()) && filterToDateOnly(dateTime) == filterToDateOnly(bucket.getDateTime()))
                return bucket;
        }
        
        SalesBucketBean bucketObj = new SalesBucketBean();
        bucketObj.setBucketNo(bucketNo);
        bucketObj.setDateTime(filterToDateOnly(dateTime));
        bucketObj.setSalesId( this.id );
        
        this.bucketList.add(bucketObj);
        
        return bucketObj;
    }
    
    public SalesBucketBean addBucket(SalesBucketBean bucketObj)
    {
        // Check duplication before Add
        for (SalesBucketBean bucket : this.bucketList)
        {
            System.err.println("SalesBean.addBucket() - [1] adding bucket " + bucket.getBucketNo() + ", " + bucket.getDateTime());
            if (bucketObj.getBucketNo().equalsIgnoreCase(bucket.getBucketNo()) && filterToDateOnly(bucketObj.getDateTime()) == filterToDateOnly(bucket.getDateTime()))
                return bucket;
        }
        
        bucketObj.setSalesId( this.id );        
        this.bucketList.add(bucketObj);
        
        return bucketObj;
    }
    
    public boolean deleteBucketBean(SalesBucketBean bucketBean)
    {
        if (bucketBean==null)
            return false;
        
        for (SalesBucketBean tmpObj : this.bucketList)
        {
            if (tmpObj.getBucketNo().equalsIgnoreCase(bucketBean.getBucketNo()))
            {
                this.bucketList.remove(tmpObj);
                return true;
            }
        }
        
        return false;
    }
    
    public void addLine(SalesLineBean lineObj)
    {
        if (lineObj==null)
            return;
        
        for (SalesLineBean tmpObj : this.lineList)
        {
            // If found similar, then update
            //if (tmpObj.getItemId().equalsIgnoreCase(lineObj.getItemId()))
            if (tmpObj.getId().equalsIgnoreCase(lineObj.getId()))
            {
                this.lineList.remove(tmpObj);
                this.lineList.add(lineObj);
                
                return;
            }
        }
        
        // Add new lineObj
        this.lineList.add(lineObj);
    }
    
    public SalesLineBean getLineBeanById(String id)
    {
        if (id == null)
            return null;
        
        for (SalesLineBean tmpObj : this.lineList)
        {
            if (tmpObj.getId().equalsIgnoreCase(id))
            {
                return tmpObj;
            }
        }
        
        return null;
    }
    
    public boolean deleteLineBean(SalesLineBean lineBean)
    {
        if (lineBean==null)
            return false;
        
        for (SalesLineBean tmpObj : this.lineList)
        {
            if (tmpObj.getId().equalsIgnoreCase(lineBean.getId()))
            {
                this.lineList.remove(tmpObj);
                return true;
            }
        }
        
        return false;
    }
    
    public String getId() { return getSafeString(this.id); }
    public String getCustomerId() { return getSafeString(this.customerId); }
    public String getCustomerName() { return getSafeString(this.customerName); }
    public int getCustomerTrip() { return this.customerTrip; }
    public long getDateTime() { return this.dateTime; }
    public String getInvoiceNo() { return getSafeString(this.invoiceNo); }
    public double getSubTotalPrice() { return this.subTotalPrice; }
    public double getTotalPrice() { return this.totalPrice; }
    public int getStatus() { return this.status; }
    public int getLineCount() { return this.lineCount; }
    public List<SalesBucketBean> getBucketList() { return this.bucketList; }
    public List<SalesLineBean> getLineList() { return this.lineList; }
    
    public void setId(String val)
    {
        this.id = val;
    }
    
    public void setCustomerId(String val) 
    {
        this.customerId = val;
    }
    
    public void setCustomerName(String val)
    {
        this.customerName = val;
    }
    
    public void setCustomerTrip(int val)
    {
        this.customerTrip = val;
    }
    
    public void setDateTime(long val) 
    {
        this.dateTime = val;
    }
    
    public void setInvoiceNo(String val)
    {
        this.invoiceNo = val;
    }
    
    public void setSubTotalPrice(double val)
    {
        this.subTotalPrice = val;
    }
    
    public void setTotalPrice(double val)
    {
        this.totalPrice = val;
    }
    
    public void setStatus(int val)
    {
        if (val != 0)
            val = 1;
        this.status = val;
    }    
    
    public void setLineCount(int val) 
    {
        this.lineCount = val;
    }
    
    public void setBucketList(List<SalesBucketBean> list)
    {
        this.bucketList = list;
    }
    
    public void setLineList(List<SalesLineBean> list)
    {
        this.lineList = list;
    }
    
    protected long filterToDateOnly(long timeInMillis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        
        return calendar.getTimeInMillis();
    }
    
    @Override
    public int compareTo(SalesBean o) {
        return new Date(this.getDateTime()).compareTo( new Date(o.getDateTime()) );
    }
}
