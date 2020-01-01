package com.ryanworks.fishery.shared.util;

import com.ryanworks.fishery.shared.bean.LogBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyLogger {

    private static java.io.PrintStream out;
    private static List<LogBean> logs;
    private static MyLogger instance = new MyLogger();

    private MyLogger() {
        logs = new ArrayList<LogBean>();
    }

    public static MyLogger getInstance() {
        return instance;
    }

    //--------------------------------------------------------------------------

    public static void setPrintOut(java.io.PrintStream o) {
        out = o;
    }

    public static void logInfo(Class className, String text) {

        if (!AppConstants.LOG_LEVEL_INFO)
            return;
        
    	if ( out == null )
            out = System.out;

        logs.add(new LogBean("System","##Type",text));

        if (out!=null)
        {            
            String message = "INFO [" + Calendar.getInstance().getTime() + "] - " + className.getSimpleName() + "." + text; 
            
            LogBook.Log(message);
            out.println(message);
        }
    }
    
    public static void logError(Class className, String text) {

        if (!AppConstants.LOG_LEVEL_ERROR)
            return;
        
    	if ( out == null )
            out = System.out;

        logs.add(new LogBean("System","##Type",text));

        if (out!=null)
        {            
            String message = "ERROR [" + Calendar.getInstance().getTime() + "] - " + className.getSimpleName() + "." + text; 
            
            LogBook.Log(message);
            out.println(message);
        }
    }

    public List<LogBean> getAllLogs() {
        return logs;
    }
}
