package com.ryanworks.fishery.server.dao;

import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.sql.rowset.serial.SerialBlob;

public class MySQLxDao {

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
    
    protected long timestampToLong(Timestamp timestamp) {
        
        if (timestamp==null)
            return 0;
        else
            return timestamp.getTime();
    }
    
    protected Timestamp longToTimestamp(long timeInMillis) {
        if (timeInMillis==0)
            return null;
        return new Timestamp(timeInMillis);
    }
    
    protected Blob byteArrayToBlob(byte[] byteArray) {
        try {
            return new SerialBlob(byteArray);
        }
        catch (Exception e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    protected byte[] blobToByteArray(Blob ablob) {
        try {
            if (ablob==null)
                return null;
            
            return ablob.getBytes(1, (int)ablob.length());
        }
        catch (SQLException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
}
