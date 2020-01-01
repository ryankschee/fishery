package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SalesDaoIntf;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSalesDaoImpl
    extends MySQLxDao
    implements SalesDaoIntf {

    @Override
    public int insertSales(SalesBean bean) 
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
                            "insert into sales_table values (?,?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getCustomerId());
                    stmt.setString(3, bean.getCustomerName());
                    stmt.setInt(4, bean.getCustomerTrip());
                    stmt.setTimestamp(5, new Timestamp(filterToDateOnly(bean.getDateTime())));
                    stmt.setString(6, bean.getInvoiceNo());      
                    stmt.setDouble(7, bean.getTotalPrice());
                    stmt.setInt(8, bean.getStatus());

                    //MyLogger.log(getClass(), "insertSales(): executing sql (" + stmt + ")");                    
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
    public boolean updateSales(SalesBean bean) 
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
                            "update sales_table set customer_id=?, customer_name=?, " +
                            "customer_trip=?, date_time=?, invoice_no=?, total_price=?, status=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getCustomerId());
                    stmt.setString(2, bean.getCustomerName());
                    stmt.setInt(3, bean.getCustomerTrip());
                    stmt.setTimestamp(4, new Timestamp(filterToDateOnly(bean.getDateTime())));
                    stmt.setString(5, bean.getInvoiceNo());  
                    stmt.setDouble(6, bean.getTotalPrice());
                    stmt.setInt(7, bean.getStatus());
                    stmt.setString(8, bean.getId()); 

                    //MyLogger.log(getClass(), "updateSales(): executing sql (" + stmt + ")");                    
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
    public boolean deleteSales(SalesBean bean) 
    {        
        if (findSingleSalesById(bean.getId())==null)
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
                            "delete from sales_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteSales(): executing sql (" + stmt + ")");                    
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
    public SalesBean findSingleSalesById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where id=?");
                stmt.setString(1, id);
               
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

    @Override
    public List<SalesBean> findSalesByCustomer(String customerId) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where customer_id=?");
                stmt.setString(1, customerId);                
                             
                ResultSet rs = stmt.executeQuery();

                List<SalesBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex) 
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());         
            return null;
        }
    }
    
    @Override
    public List<SalesBean> findSalesByDate(long timeInMillis) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where date(date_time)=?");
                stmt.setDate(1, new Date(timeInMillis));                
                ResultSet rs = stmt.executeQuery();

                List<SalesBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex) 
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());    
            return null;
        }
    }
    
    @Override
    public List<SalesBean> findSalesByStatusAndDateRange(int status, long startDate, long endDate) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where status=? and date(date_time) between ? and ?");      
                stmt.setInt(1, status);
                stmt.setDate(2, new Date(startDate));                
                stmt.setDate(3, new Date(endDate)); 
                
                MyLogger.logInfo(getClass(), "findSalesByStatusAndDateRange(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex) 
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());      
            return null;
        }
    }
    
    @Override
    public SalesBean findSalesByCustomerAndDate(String customerId, long dateTime) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where customer_id=? and date(date_time)=? order by customer_trip asc");
                stmt.setString(1, customerId);         
                stmt.setDate(2, new Date(dateTime));                
                
                //MyLogger.log(getClass(), "findSalesByCustomerAndDate(): executing sql (" + stmt + ")");                
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
    
    @Override
    public List<SalesBean> findSalesByDateRange(long startTime, long endTime) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where date(date_time) between ? and ? order by date_time asc");
                stmt.setDate(1, new Date(startTime));   
                stmt.setDate(2, new Date(endTime));               
                ResultSet rs = stmt.executeQuery();

                List<SalesBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex) 
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());           
            return null;
        }
    }
    
    @Override
    public List<SalesBean> findSalesByCustomerAndDateRange(String customerId, long startTime, long endTime) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_table where customer_id=? and date(date_time) between ? and ? order by date_time asc");
                stmt.setString(1, customerId);         
                stmt.setDate(2, new Date(startTime));   
                stmt.setDate(3, new Date(endTime));               
                
                //MyLogger.log(getClass(), "findSalesByCustomerAndDateRange(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                return alist;
            }
        }
        catch (Exception ex) 
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());           
            return null;
        }
    }
    
    @Override
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return -1;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select count(*) from sales_table where customer_id=? and date_time=?");
                stmt.setString(1, customerId);
                stmt.setTimestamp(2, new Timestamp(dateTime));
                //MyLogger.log(getClass(), "getTripNoByCustomerIdAndDate(): executing sql (" + stmt + ")");    
                
                ResultSet rs = stmt.executeQuery();
                rs.next();
                
                return rs.getInt(1);
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return -1;
        }
    }
    
    @Override
    public int updateCustomer(CustomerBean customerObj) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return 0;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "update sales_table set customer_name=? " +
                        "where customer_id=?" );
                stmt.setString(1, customerObj.getName());
                stmt.setString(2, customerObj.getId());

                //MyLogger.log(getClass(), "updateCustomer(): executing sql (" + stmt + ")");                    
                return stmt.executeUpdate();
            }
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return 0;
        }
    }

    protected SalesBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SalesBean bean = new SalesBean();
            
            bean.setId(rs.getString(1));
            bean.setCustomerId(rs.getString(2));
            bean.setCustomerName(rs.getString(3));
            bean.setCustomerTrip(rs.getInt(4));
            bean.setDateTime(rs.getTimestamp(5).getTime());
            bean.setInvoiceNo(rs.getString(6));
            bean.setTotalPrice(rs.getDouble(7));
            bean.setStatus(rs.getInt(8));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
