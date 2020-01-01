package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.CustomerDaoIntf;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLCustomerDaoImpl
    extends MySQLxDao
    implements CustomerDaoIntf {

    public int insertCustomer(CustomerBean bean) 
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
                            "insert into customer_table values (?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getName());
                    stmt.setString(3, bean.getNotes());
                    stmt.setDouble(4, bean.getBalance());
                    stmt.setTimestamp(5, this.longToTimestamp(bean.getBalanceLastUpdate()));
                    stmt.setBoolean(6, bean.isChanged());

                    //MyLogger.log(getClass(), "insertCustomer(): executing sql (" + stmt + ")");                    
                    return stmt.executeUpdate();
                }
            }
            catch (Exception ex) {
                MyLogger.logError(getClass(), "exception: " + ex.getMessage());
                return -1;
            }
        }
    }

    public boolean updateCustomer(CustomerBean bean) 
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
                            "update customer_table set name=?, notes=?, balance=?, balance_last_update=?, changed=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getName());
                    stmt.setString(2, bean.getNotes());
                    stmt.setDouble(3, bean.getBalance());
                    stmt.setTimestamp(4, this.longToTimestamp(bean.getBalanceLastUpdate()));
                    stmt.setBoolean(5, bean.isChanged());
                    stmt.setString(6, bean.getId());

                    //MyLogger.log(getClass(), "updateCustomer(): executing sql (" + stmt + ")");                    
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

    public boolean deleteCustomer(CustomerBean bean) 
    {        
        if (findSingleCustomerById(bean.getId())==null)
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
                            "delete from customer_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteCustomer(): executing sql (" + stmt + ")");                    
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
    public List<CustomerBean> getAllCustomers() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_table order by id");
                //MyLogger.log(getClass(), "getAllCustomers(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return new ArrayList();
        }
    }
    
    @Override
    public CustomerBean findSingleCustomerById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleCustomerById(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();
                if (rs.next() == true)
                    return this.toBeanObject(rs);
                else
                    return null;
            }
        }
        catch (Exception ex) {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public List<CustomerBean> findCustomersByDate(long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where date_time=?");
                stmt.setTimestamp(1, new Timestamp(dateTime));
                //MyLogger.log(getClass(), "[1] findCustomersByDate(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                HashMap<String, CustomerBean> map = new HashMap();
                while (rs.next()) {
                    
                    PreparedStatement stmt1 =
                        MySQLConnector.getInstance().getConnection().prepareStatement(
                            "select * from customer_table where id=? order by id" );
                    stmt1.setString(1, rs.getString("customer_id"));
                    //MyLogger.log(getClass(), "[2] findCustomersByDate(): executing sql (" + stmt1 + ")"); 
                    
                    ResultSet rs1 = stmt1.executeQuery();
                    
                    if (rs1.next()) 
                    {
                        CustomerBean customer = this.toBeanObject(rs1);
                        map.put(customer.getId(), customer);
                    }
                }
                
                List<CustomerBean> alist = new ArrayList<>();
                alist.addAll(map.values()); 
                
                //MyLogger.log(getClass(), "[3] return alist (" + alist + ", " + alist.size() + ")");                
                                
                return alist;
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return new ArrayList();
        }
    }
    
    protected CustomerBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            CustomerBean bean = new CustomerBean();
            
            bean.setId(rs.getString(1));
            bean.setName(rs.getString(2));
            bean.setNotes(rs.getString(3));
            bean.setBalance(rs.getDouble(4));
            bean.setBalanceLastUpdate(this.timestampToLong(rs.getTimestamp(5)));
            bean.setChanged(rs.getBoolean(6));
            
            return bean;
        }
        catch (Exception ex) 
        {
            return null;
        }
    }
}
