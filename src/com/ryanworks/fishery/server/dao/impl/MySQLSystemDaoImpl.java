package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SystemDaoIntf;
import com.ryanworks.fishery.shared.bean.SystemBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;

public class MySQLSystemDaoImpl
    extends MySQLxDao
    implements SystemDaoIntf {

    @Override
    public boolean updateSystem(SystemBean bean) 
    {
        if (!bean.isValid())
            return false;
        else 
        {
            try 
            {
                if(MySQLConnector.getInstance().getConnection()==null)
                    return false;
                else 
                {
                    PreparedStatement stmt =
                        MySQLConnector.getInstance().getConnection().prepareStatement(
                            "update system_table set in_transaction_id=?, sales_id=? " +
                            "where id='system'" );
                    stmt.setInt(1, bean.getInTransactionId());
                    stmt.setInt(2, bean.getSalesId());

                    //MyLogger.log(getClass(), "updateSystem(): executing sql (" + stmt + ")");                    
                    return (stmt.executeUpdate()!=0);
                }
            }
            catch (Exception ex) 
            {
                MyLogger.logError(getClass(), "exception: " + ex.getMessage());
                return false;
            }
        }
    }

    @Override
    public SystemBean getSystemBean() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from system_table");
                //MyLogger.log(getClass(), "getSystemBean(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                rs.next();
                return this.toBeanObject(rs);
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    protected SystemBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SystemBean bean = new SystemBean();
            
            bean.setId(rs.getString(1));
            bean.setInTransactionId(rs.getInt(2));
            bean.setSalesId(rs.getInt(3));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
