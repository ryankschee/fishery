package com.ryanworks.fishery.server.dao;

import com.ryanworks.fishery.shared.util.AbstractPropertiesLoader;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

public class MySQLConnector 
    extends AbstractPropertiesLoader {
    
    public static long lastConnectionEstablished = 0L;
    private static Connection conn = null;

    private final String PROPS_SERVER;
    private final String PROPS_PORT;
    private final String PROPS_DATABASE;
    private final String PROPS_USERNAME;
    private final String PROPS_PASSWORD;
    
    private static MySQLConnector instance = new MySQLConnector();
    
    private MySQLConnector() {
        Properties props = loadProperties("com.ryanworks.fishery.server.dao.dbconfig");
        
        PROPS_SERVER    = props.getProperty( "PROPS_SERVER" );
        PROPS_PORT      = props.getProperty( "PROPS_PORT" );
        PROPS_DATABASE  = props.getProperty( "PROPS_DATABASE" );
        PROPS_USERNAME  = props.getProperty( "PROPS_USERNAME" );
        PROPS_PASSWORD  = props.getProperty( "PROPS_PASSWORD" );
    }
    
    public static MySQLConnector getInstance() 
    { 
        return instance;
    }
    
    protected String getDBUrl() {
        return "jdbc:mysql://" + PROPS_SERVER + ":" + PROPS_PORT + "/" + PROPS_DATABASE;
    }
    
    // returns a Statement on successful connection
    public void connect()
        throws SQLException, Exception
    {        
        MyLogger.logInfo(getClass(), "Connecting to DB: " + getDBUrl());
        
        // try to make a JDBC connection to the "test" database
        Class.forName( "org.gjt.mm.mysql.Driver" ).newInstance();
        conn = DriverManager.getConnection( getDBUrl(), PROPS_USERNAME, PROPS_PASSWORD);
        lastConnectionEstablished = Calendar.getInstance().getTimeInMillis();
        
        MyLogger.logInfo(getClass(), "DB Connection OK.");
    }

    public Connection getConnection() 
    {
        boolean getConnectionNow = false;
        
        if (conn==null)
            getConnectionNow = true;
        
        // Check if last get connection is more than 30 minutes (30m x 60s x 1000ms)
        long now = Calendar.getInstance().getTimeInMillis();
        if ( (now - lastConnectionEstablished) > 30*60*1000 )    
            getConnectionNow = true;
                    
        if (getConnectionNow) {
            try 
            {
                connect();
            }
            catch (Exception ex) 
            {                
                MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            }
        }

        return conn;
    }
}
