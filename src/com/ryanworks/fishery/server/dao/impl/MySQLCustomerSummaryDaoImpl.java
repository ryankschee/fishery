package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.CustomerSummaryDaoIntf;
import com.ryanworks.fishery.shared.bean.CustomerSummaryBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MySQLCustomerSummaryDaoImpl
    extends MySQLxDao
    implements CustomerSummaryDaoIntf {

    @Override
    public int insertSummary(CustomerSummaryBean bean) 
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
                    Calendar firstDay = Calendar.getInstance();
                    firstDay.set(Calendar.YEAR, bean.getYear());
                    firstDay.set(Calendar.MONTH, bean.getMonth() - 1);
                    firstDay.set(Calendar.DATE, 1);
                    firstDay.set(Calendar.HOUR, 0);
                    firstDay.set(Calendar.MINUTE, 0);
                    firstDay.set(Calendar.SECOND, 1);
                                        
                    PreparedStatement stmt =
                        MySQLConnector.getInstance().getConnection().prepareStatement(
                            "insert into customer_summary_table values (?,?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getCustomerId());
                    stmt.setTimestamp(3, this.longToTimestamp(firstDay.getTimeInMillis()));
                    stmt.setInt(4, bean.getYear());
                    stmt.setInt(5, bean.getMonth());
                    stmt.setDouble(6, bean.getBalance());
                    stmt.setDouble(7, bean.getTotalSalesAmount());
                    stmt.setDouble(8, bean.getTotalPaymentAmount());

                    //MyLogger.log(getClass(), "insertSummary(): executing sql (" + stmt + ")");                    
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
    public boolean updateSummary(CustomerSummaryBean bean) 
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
                            "update customer_summary_table set customer_id=?, first_day=?, year=?, month=?, " +
                            "balance=?, total_sales_amount=?, total_payment_amount=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getCustomerId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getFirstDay()));
                    stmt.setInt(3, bean.getYear());
                    stmt.setInt(4, bean.getMonth());
                    stmt.setDouble(5, bean.getBalance());
                    stmt.setDouble(6, bean.getTotalSalesAmount());
                    stmt.setDouble(7, bean.getTotalPaymentAmount());
                    stmt.setString(8, bean.getId());

                    //MyLogger.log(getClass(), "updateSummary(): executing sql (" + stmt + ")");                    
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
    public boolean deleteSummary(CustomerSummaryBean bean) 
    {        
        if (findSingleSummaryById(bean.getId())==null)
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
                            "delete from customer_summary_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteSummary(): executing sql (" + stmt + ")");                    
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
    public boolean deleteSummaryByCustomer(String customerId) 
    {        
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from customer_summary_table where customer_id=?");
                stmt.setString(1, customerId);

                //MyLogger.log(getClass(), "deleteSummary(): executing sql (" + stmt + ")");                    
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
    public CustomerSummaryBean findSingleSummaryById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_summary_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleSummaryById(): executing sql (" + stmt + ")");                
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
    public List<CustomerSummaryBean> findSummaryByCustomer(String customerId)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_summary_table where customer_id=?");
                stmt.setString(1, customerId);
                //MyLogger.log(getClass(), "[1] findSummaryByCustomer(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerSummaryBean> alist = new ArrayList<>();
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
    public CustomerSummaryBean findSummaryByCustomerAndDate(String customerId, int year, int month)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_summary_table where customer_id=? and year=? and month=?");
                stmt.setString(1, customerId);
                stmt.setInt(2, year);
                stmt.setInt(3, month);
                //MyLogger.log(getClass(), "[1] findSummaryByCustomerAndDate(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerSummaryBean> alist = new ArrayList<>();
                if (rs.next())
                    return this.toBeanObject(rs);
                else
                {
                    CustomerSummaryBean bean = new CustomerSummaryBean();
            
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 1);
                    bean.setFirstDay(calendar.getTimeInMillis());
                    
                    bean.setId("");
                    bean.setCustomerId(customerId);
                    bean.setYear(year);
                    bean.setMonth(month);
                    bean.setBalance(0.0d);
                    bean.setTotalPaymentAmount(0.0d);
                    bean.setTotalSalesAmount(0.0d);
                    
                    return bean;
                }
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public List<CustomerSummaryBean> findSummaryByCustomerBeforeDate(String customerId, long anyday)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from customer_summary_table where customer_id=? and first_day <= ?");
                stmt.setString(1, customerId);
                stmt.setTimestamp(2, this.longToTimestamp(anyday));
                //MyLogger.log(getClass(), "[1] findSummaryByCustomerBeforeDate(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<CustomerSummaryBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    protected CustomerSummaryBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            CustomerSummaryBean bean = new CustomerSummaryBean();
            
            bean.setId(rs.getString(1));
            bean.setCustomerId(rs.getString(2));
            bean.setFirstDay(this.timestampToLong(rs.getTimestamp(3)));
            bean.setYear(rs.getInt(4));
            bean.setMonth(rs.getInt(5));
            bean.setBalance(rs.getDouble(6));
            bean.setTotalSalesAmount(rs.getDouble(7));
            bean.setTotalPaymentAmount(rs.getDouble(8));
                        
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
