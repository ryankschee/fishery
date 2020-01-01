package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SalesLineDaoIntf;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSalesLineDaoImpl
    extends MySQLxDao
    implements SalesLineDaoIntf {

    @Override
    public int insertSalesLine(SalesLineBean bean) 
    {
        if (!bean.isValid() || bean.isSaveable()==false)
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
                            "insert into sales_line_table values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getItemId());
                    stmt.setString(3, bean.getItemName());
                    stmt.setString(4, bean.getItemNewName());
                    stmt.setString(5, bean.getItemNameBm());
                    stmt.setDouble(6, bean.getWeight());
                    stmt.setDouble(7, bean.getUnitPrice());
                    stmt.setDouble(8, bean.getSavingPrice());
                    stmt.setString(9, bean.getBucketNo());
                    stmt.setString(10, bean.getSupplierId());
                    stmt.setString(11, bean.getSupplierName());
                    stmt.setTimestamp(12, new Timestamp(super.filterToDateOnly(bean.getDateTime())));
                    stmt.setString(13, bean.getCustomerId());
                    stmt.setString(14, bean.getSalesId());
                    stmt.setDouble(15, bean.getAddWeight());

                    //MyLogger.log(getClass(), "insertSalesLine(): executing sql (" + stmt + ")");                    
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
    public boolean updateSalesLine(SalesLineBean bean) 
    {
        if (!bean.isValid() || bean.isSaveable()==false)
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
                            "update sales_line_table set item_id=?, item_name=?, item_new_name=?, item_name_bm=?, weight=?, unit_price=?, saving_price=?, " +
                            "bucket_no=?, supplier_id=?, supplier_name=?, date_time=?, customer_id=?, sales_id=?, add_weight=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getItemId());
                    stmt.setString(2, bean.getItemName());
                    stmt.setString(3, bean.getItemNewName());
                    stmt.setString(4, bean.getItemNameBm());
                    stmt.setDouble(5, bean.getWeight());
                    stmt.setDouble(6, bean.getUnitPrice());
                    stmt.setDouble(7, bean.getSavingPrice());
                    stmt.setString(8, bean.getBucketNo());
                    stmt.setString(9, bean.getSupplierId());
                    stmt.setString(10, bean.getSupplierName());
                    stmt.setTimestamp(11, new Timestamp(super.filterToDateOnly(bean.getDateTime())));
                    stmt.setString(12, bean.getCustomerId());
                    stmt.setString(13, bean.getSalesId());
                    stmt.setDouble(14, bean.getAddWeight());
                    stmt.setString(15, bean.getId());

                    //MyLogger.log(getClass(), "updateSalesLine(): executing sql (" + stmt + ")");                    
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
    public int updateItemName(ItemBean itemObj) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return 0;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "update sales_line_table set item_name_bm=? " +
                        "where item_id=?" );
                stmt.setString(1, itemObj.getNameBm());
                stmt.setString(2, itemObj.getId());

                //MyLogger.log(getClass(), "updateItemName(): executing sql (" + stmt + ")");                    
                return stmt.executeUpdate();
            }
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return 0;
        }
    }

    @Override
    public boolean deleteSalesLine(SalesLineBean bean) 
    {        
        if (findSingleSalesLineById(bean.getId())==null)
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
                            "delete from sales_line_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    MyLogger.logInfo(getClass(), "deleteSalesLine(): executing sql (" + stmt + ")");                    
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
    public SalesLineBean findSingleSalesLineById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_line_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleSalesLineById(): executing sql (" + stmt + ")");                
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
    public SalesLineBean findSingleSalesLineByCustomerBucketDate(String customerId, String bucketNo, long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_line_table where customer_id=? and bucket_no=? and date_time=?");
                stmt.setString(1, customerId);
                stmt.setString(2, bucketNo);
                stmt.setTimestamp(3, new Timestamp(super.filterToDateOnly(dateTime)));   

                //MyLogger.log(getClass(), "findSingleSalesLineByCustomerBucketDate(): executing sql (" + stmt + ")");                
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
    public List<SalesLineBean> findSalesLinesBySalesId(String salesId) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_line_table where sales_id=?");
                stmt.setString(1, salesId);                
                
                //MyLogger.log(getClass(), "findSalesLinesByInSalesId(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesLineBean> alist = new ArrayList<SalesLineBean>();
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
    public List<SalesLineBean> findSalesLinesBySalesIdAndBucketNo(String salesId, String bucketNo) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_line_table where sales_id=? and bucket_no=?");
                stmt.setString(1, salesId);                
                stmt.setString(2, bucketNo);                
                
                //MyLogger.log(getClass(), "findSalesLinesBySalesIdAndBucketNo(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesLineBean> alist = new ArrayList<SalesLineBean>();
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
    public List<SalesLineBean> findSalesLinesByCustomerIdAndDate(String customerId, long date) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_line_table where customer_id=? and date_time=?");
                stmt.setString(1, customerId);                
                stmt.setTimestamp(2, new Timestamp(super.filterToDateOnly(date)));                
                
                //MyLogger.log(getClass(), "findSalesLinesByCustomerIdAndDate(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesLineBean> alist = new ArrayList<SalesLineBean>();
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
    public List<SalesLineBean> findSalesLinesByGroup(String salesId, String itemNewName, String bucketNo) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from sales_line_table where sales_id=? and item_new_name=? and bucket_no=?");
                stmt.setString(1, salesId);                
                stmt.setString(2, itemNewName);               
                stmt.setString(3, bucketNo);                
                
                MyLogger.logInfo(getClass(), "findSalesLinesByGroup(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<SalesLineBean> alist = new ArrayList<SalesLineBean>();
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
    
    protected SalesLineBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SalesLineBean bean = new SalesLineBean();
            
            bean.setId(rs.getString(1));
            bean.setItemId(rs.getString(2));
            bean.setItemName(rs.getString(3));
            bean.setItemNewName(rs.getString(4));
            bean.setItemNameBm(rs.getString(5));
            bean.setWeight(rs.getDouble(6));
            bean.setUnitPrice(rs.getDouble(7));
            bean.setSavingPrice(rs.getDouble(8));
            bean.setBucketNo(rs.getString(9));
            bean.setSupplierId(rs.getString(10));
            bean.setSupplierName(rs.getString(11));
            bean.setDateTime(super.filterToDateOnly(rs.getTimestamp(12).getTime()));
            bean.setCustomerId(rs.getString(13));
            bean.setSalesId(rs.getString(14));
            bean.setAddWeight(rs.getDouble(15));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
