package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLUserDaoImpl;
import com.ryanworks.fishery.shared.bean.UserBean;
import com.ryanworks.fishery.shared.service.UserServiceIntf;
import com.ryanworks.fishery.shared.util.MyLogger;

public class UserServiceHandler
    extends MyServiceHandler
    implements UserServiceIntf 
{
    public UserBean authenticate(String username, String password) 
    {
        if (username==null || password==null)
            return null;
        if ("".equalsIgnoreCase(username) || "".equalsIgnoreCase(password))
            return null;

        MyLogger.logInfo(getClass(), "authenticate(): EmployeeID & Password checked.");

        try 
        {
            // Get User DAO object
            MySQLUserDaoImpl dao = this.getDaoFactoryObject().getUserDao();            
            MyLogger.logInfo(getClass(), "authenticate(): obtained Data Access Object.");
            
            // Find EmployeeBean object by given EmployeeId
            UserBean user = dao.findSingleUserByUsername( username );

            if (user!=null && password.equalsIgnoreCase(user.getPassword())) 
            {
                MyLogger.logInfo(getClass(), "authenticate(): EmployeeId and Password are correct.");
                return user;
            }
            else 
            {
                MyLogger.logInfo(getClass(), "authenticate(): No EmployeeBean object found.");
                return null;
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}