package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.UserDaoIntf;
import com.ryanworks.fishery.shared.bean.UserBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;

public class MySQLUserDaoImpl
    extends MySQLxDao
    implements UserDaoIntf {

    public int insertUser(UserBean bean) 
    {
        if (!bean.isValid())
            return -1;
        else 
        {
            try 
            {
                if(MySQLConnector.getInstance().getConnection()==null)
                    return -1;
                else 
                {
                    PreparedStatement stmt =
                        MySQLConnector.getInstance().getConnection().prepareStatement(
                            "insert into user_table values (?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getUsername());
                    stmt.setString(3, bean.getPassword());
                    stmt.setTimestamp(4, longToTimestamp(bean.getLastLogin()));

                    //MyLogger.log(getClass(), "insertUser(): executing sql (" + stmt + ")");                    
                    return stmt.executeUpdate();
                }
            }
            catch (Exception ex) {
                MyLogger.logError(getClass(), "exception: " + ex.getMessage());
                return -1;
            }
        }
    }

    public boolean updateUser(UserBean bean) 
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
                            "update user_table set username=?, password=?, " +
                            "last_login=? where id=?" );
                    stmt.setString(1, bean.getPassword());
                    stmt.setString(2, bean.getPassword());
                    stmt.setTimestamp(3, new Timestamp(bean.getLastLogin()));
                    stmt.setString(4, bean.getId());

                    //MyLogger.log(getClass(), "updateUser(): executing sql (" + stmt + ")");                    
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

    public boolean deleteUser(UserBean bean) 
    {        
        if (findSingleUserById(bean.getId())==null)
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
                            "delete from user_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteUser(): executing sql (" + stmt + ")");                    
                    return (stmt.executeUpdate()!=0);
                }
            }
            catch (Exception ex) {
                MyLogger.logError(getClass(), "exception: " + ex.getMessage());
                return false;
            }
        }
    }
    

    public UserBean findSingleUserById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from user_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleUserById(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();
                return this.toBeanObject(rs);
            }
        }
        catch (Exception ex) {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }

    public UserBean findSingleUserByUsername(String username) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from user_table where username=?");                
                stmt.setString(1, username);

                //MyLogger.log(getClass(), "findSingleUserByUsername(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                if (rs.next())
                    return this.toBeanObject(rs);
                else 
                    return null;
            }
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());      
            return null;
        }
    }

    protected UserBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            UserBean user = new UserBean();
            
            user.setId(rs.getString(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setLastLogin(timestampToLong(rs.getTimestamp(4)));
            
            return user;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
