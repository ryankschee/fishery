package com.ryanworks.fishery.shared.bean;

import java.util.Calendar;

public class CustomerReportLineBean 
    implements Comparable<CustomerReportLineBean>
{
    private long dateTime;
    private Object lineObject;

    public CustomerReportLineBean(long dateTime, Object lineObject)
    {
        this.dateTime = dateTime;
        this.lineObject = lineObject;
    }

    public Calendar getDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTime);
        return cal;
    }

    public Object getLineObject()
    {
        return this.lineObject;
    }
    
    public void setDateTime(long val) {
        this.dateTime = val;
    }
    
    public void setLineObject(Object val)
    {
        this.lineObject = val;
    }

    @Override
    public int compareTo(CustomerReportLineBean o) {
        if (getDateTime() == null || o.getDateTime() == null)
            return 0;
        return getDateTime().compareTo(o.getDateTime());
    }
}