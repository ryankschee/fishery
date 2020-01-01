package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.CustomerPaymentDaoIntf;
import com.ryanworks.fishery.shared.bean.CustomerPaymentBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCustomerPaymentDaoImpl
    extends MySQLxDao
    implements CustomerPaymentDaoIntf {

    @Override
    public int insertPayment(CustomerPaymentBean bean) 
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
                            "insert into customer_payment_table values (?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getCustomerId());
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getDate()));
                    stmt.setDouble(4, bean.getAmount());
                    stmt.setString(5, bean.getTerm());
                    stmt.setString(6, bean.getRemarks());

                    //MyLogger.log(getClass(), "insertPayment(): executing sql (" + stmt + ")");                    
                    return stmt.executeUpdate();
                }
            }
            catch (Exception ex) {
                MyLogger.logError(getClass(), "exception: " + ex.getMessage());
                return -1;
            }
        }
    }

    @Override
    public boolean updatePayment(CustomerPaymentBean bean) 
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
                            "update customer_payment_table set customer_id=?, date=?, amount=?, term=?, remarks=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getCustomerId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getDate()));
                    stmt.setDouble(3, bean.getAmount());
                    stmt.setString(4, bean.getTerm());
                    stmt.setString(5, bean.getRemarks());
                    stmt.setString(6, bean.getId());

                    //MyLogger.log(getClass(), "updatePayment(): executing sql (" + stmt + ")");                    
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
    public boolean deletePayment(CustomerPaymentBean bean) 
    {        
        if (findSinglePaymentById(bean.getId())==null)
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
                            "delete from customer_payment_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deletePayment(): executing sql (" + stmt + ")");                    
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
    public boolean deletePaymentsByCustomer(String customerId) 
    {     
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from customer_payment_table where customer_id=?");
                stmt.setString(1, customerId);

                //MyLogger.log(getClass(), "deletePayment(): executing sql (" + stmt + ")");                    
                return (stmt.executeUpdate()!=0);
            }
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return false;
        }
    }
    
    @Override
    public CustomerPaymentBean findSinglePaymentById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_payment_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSinglePaymentById(): executing sql (" + stmt + ")");                
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
    public List<CustomerPaymentBean> findPaymentsByCustomer(String customerId)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_payment_table where customer_id=?");
                stmt.setString(1, customerId);
                //MyLogger.log(getClass(), "[1] findPaymentsByCustomer(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerPaymentBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(toBeanObject(rs));
                
                //MyLogger.log(getClass(), "[2] return alist (" + alist + ", " + alist.size() + ")");                
                                
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
    public List<CustomerPaymentBean> findPaymentsByCustomerAndDateRange(String customerId, long startTime, long endTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_payment_table where customer_id=? and date between ? and ?");
                stmt.setString(1, customerId);
                stmt.setTimestamp(2, this.longToTimestamp(startTime));
                stmt.setTimestamp(3, this.longToTimestamp(endTime));
                //MyLogger.log(getClass(), "[1] findPaymentsByCustomerAndDateRange(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerPaymentBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(toBeanObject(rs));
                
                //MyLogger.log(getClass(), "[2] return alist (" + alist + ", " + alist.size() + ")");                
                                
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
    public List<CustomerPaymentBean> findPaymentsByCustomerBeforeDate(String customerId, long anyday)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_payment_table where customer_id=? and date < ?");
                stmt.setString(1, customerId);
                stmt.setTimestamp(2, this.longToTimestamp(anyday));
                //MyLogger.log(getClass(), "[1] findPaymentsByCustomerBeforeDate(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerPaymentBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(toBeanObject(rs));
                
                //MyLogger.log(getClass(), "[2] return alist (" + alist + ", " + alist.size() + ")");                
                                
                return alist;
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return new ArrayList();
        }
    }
    
    protected CustomerPaymentBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            CustomerPaymentBean bean = new CustomerPaymentBean();
            
            bean.setId(rs.getString(1));
            bean.setCustomerId(rs.getString(2));
            bean.setDate(this.timestampToLong(rs.getTimestamp(3)));
            bean.setAmount(rs.getDouble(4));
            bean.setTerm(rs.getString(5));
            bean.setRemarks(rs.getString(6));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
