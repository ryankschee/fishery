package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SalesBucketDaoIntf;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSalesBucketDaoImpl
    extends MySQLxDao
    implements SalesBucketDaoIntf {

    @Override
    public int insertSalesBucket(SalesBucketBean bean) 
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
                            "insert into sales_bucket_table values (?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getBucketNo());
                    stmt.setTimestamp(3, new Timestamp(filterToDateOnly(bean.getDateTime())));
                    stmt.setString(4, bean.getCustomerId());                    
                    stmt.setString(5, bean.getSalesId());                    

                    //MyLogger.log(getClass(), "insertSalesBucket(): executing sql (" + stmt + ")");                    
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
    public boolean updateSalesBucket(SalesBucketBean bean) 
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
                            "update sales_bucket_table set bucket_no=?, date_time=?, customer_id=?, sales_id=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getBucketNo());
                    stmt.setTimestamp(2, new Timestamp(filterToDateOnly(bean.getDateTime())));
                    stmt.setString(3, bean.getCustomerId());                    
                    stmt.setString(4, bean.getSalesId());    
                    stmt.setString(5, bean.getId()); 
                
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
    public boolean deleteSalesBucket(SalesBucketBean bean) 
    {        
        if (findSingleSalesBucketById(bean.getId())==null)
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
                            "delete from sales_bucket_table where id=?");
                    stmt.setString(1, bean.getId());
                     
                    MyLogger.logInfo(getClass(), "deleteSalesBucket(): executing sql (" + stmt + ")");                    
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
    public SalesBucketBean findSingleSalesBucketById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_bucket_table where id=?");
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
    public SalesBucketBean findSingleSalesBucketByCustomerBucketDate(String customerId, String bucketNo, long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_bucket_table where customer_id=? and bucket_no=? and date(date_time)=?");
                stmt.setString(1, customerId);
                stmt.setString(2, bucketNo);
                stmt.setDate(3, new Date(dateTime));                   
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next())
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
    public List<SalesBucketBean> findSalesBucketByDate(long timeInMillis) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_bucket_table where date(date_time)=?");
                stmt.setDate(1, new Date(timeInMillis));                              
                ResultSet rs = stmt.executeQuery();

                List<SalesBucketBean> alist = new ArrayList<>();
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
    public List<SalesBucketBean> findSalesBucketBySalesId(String salesId) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_bucket_table where sales_id=?");
                stmt.setString(1, salesId);                
                
                //MyLogger.log(getClass(), "findSalesBucketBySalesId(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesBucketBean> alist = new ArrayList<>();
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
    public List<SalesBucketBean> findSalesBucketByCustomerIdAndDate(String customerId, long date) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_bucket_table where customer_id=? and date_time=?");
                stmt.setString(1, customerId);                
                stmt.setTimestamp(2, new Timestamp(super.filterToDateOnly(date)));                
                
                //MyLogger.log(getClass(), "findSalesBucketByCustomerIdAndDate(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesBucketBean> alist = new ArrayList<SalesBucketBean>();
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
    
    protected SalesBucketBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SalesBucketBean bean = new SalesBucketBean();
            
            bean.setId(rs.getString(1));
            bean.setBucketNo(rs.getString(2));
            bean.setDateTime(rs.getTimestamp(3).getTime());
            bean.setCustomerId(rs.getString(4));
            bean.setSalesId(rs.getString(5));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
