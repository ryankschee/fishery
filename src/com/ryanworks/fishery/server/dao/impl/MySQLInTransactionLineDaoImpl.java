package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.InTransactionLineDaoIntf;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLInTransactionLineDaoImpl
    extends MySQLxDao
    implements InTransactionLineDaoIntf {

    @Override
    public int insertInTransactionLine(InTransactionLineBean bean) 
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
                            "insert into in_transaction_line_table values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getItemId());
                    stmt.setString(3, bean.getItemName());
                    stmt.setString(4, bean.getItemNewName());
                    stmt.setString(5, bean.getItemNameBm());
                    stmt.setDouble(6, bean.getWeight());
                    stmt.setDouble(7, bean.getUnitPrice());
                    stmt.setString(8, bean.getBucketNo());
                    stmt.setString(9, bean.getCustomerId());
                    stmt.setString(10, bean.getCustomerName());
                    stmt.setString(11, bean.getInTransactionId());
                    stmt.setTimestamp(12, this.longToTimestamp(bean.getDateTime()));
                    stmt.setString(13, bean.getSupplierId());
                    stmt.setDouble(14, bean.getSaving());
                    stmt.setString(15, bean.getSalesId());
                    stmt.setString(16, bean.getSalesLineId());

                    MyLogger.logInfo(getClass(), "insertInTransactionLine(): executing sql (" + stmt + ")");                    
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
    public boolean updateInTransactionLine(InTransactionLineBean bean) 
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
                            "update in_transaction_line_table set item_id=?, item_name=?, item_new_name=?, item_name_bm=?, weight=?, " +
                            "unit_price=?, bucket_no=?, customer_id=?, customer_name=?, in_transaction_id=?, date_time=?, " + 
                            "supplier_id=?, saving=?, sales_id=?, sales_line_id=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getItemId());
                    stmt.setString(2, bean.getItemName());
                    stmt.setString(3, bean.getItemNewName());
                    stmt.setString(4, bean.getItemNameBm());
                    stmt.setDouble(5, bean.getWeight());
                    stmt.setDouble(6, bean.getUnitPrice());
                    stmt.setString(7, bean.getBucketNo());
                    stmt.setString(8, bean.getCustomerId());
                    stmt.setString(9, bean.getCustomerName());
                    stmt.setString(10, bean.getInTransactionId());
                    stmt.setTimestamp(11, this.longToTimestamp(bean.getDateTime()));
                    stmt.setString(12, bean.getSupplierId());
                    stmt.setDouble(13, bean.getSaving());
                    stmt.setString(14, bean.getSalesId());
                    stmt.setString(15, bean.getSalesLineId());
                    stmt.setString(16, bean.getId());

                    MyLogger.logInfo(getClass(), "updateInTransactionLine(): executing sql (" + stmt + ")");                    
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
                        "update in_transaction_line_table set item_name_bm=? " +
                        "where item_id=?" );
                stmt.setString(1, itemObj.getNameBm());
                stmt.setString(2, itemObj.getId());

                MyLogger.logInfo(getClass(), "updateItemName(): executing sql (" + stmt + ")");                    
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
    public boolean deleteInTransactionLine(InTransactionLineBean bean) 
    {        
        if (findSingleInTransactionLineById(bean.getId())==null)
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
                            "delete from in_transaction_line_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    MyLogger.logInfo(getClass(), "deleteInTransactionLine(): executing sql (" + stmt + ")");                    
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
    public InTransactionLineBean findSingleInTransactionLineById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_line_table where id=?");
                stmt.setString(1, id);

                MyLogger.logInfo(getClass(), "findSingleInTransactionLineById(): executing sql (" + stmt + ")");                
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
    public InTransactionLineBean findSingleInTransactionLineBySalesLineId(String salesLineId) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_line_table where sales_line_id=?");
                stmt.setString(1, salesLineId);

                MyLogger.logInfo(getClass(), "findSingleInTransactionLineBySalesLineId(): executing sql (" + stmt + ")");                
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
    public List<InTransactionLineBean> findInTransactionLinesByInTransactionId(String inTransactionId) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_line_table where in_transaction_id=?");
                stmt.setString(1, inTransactionId);                
                
                MyLogger.logInfo(getClass(), "findInTransactionLinesByInTransactionId(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<InTransactionLineBean> alist = new ArrayList<>();
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
    public List<InTransactionLineBean> findInTransactionLinesByItemAndDate(ItemBean item, long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_line_table where item_id=? and date(date_time)=?");
                stmt.setString(1, item.getId());   
                stmt.setDate(2, new Date(dateTime));
                
                MyLogger.logInfo(getClass(), "findInTransactionLinesByItemAndDate(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<InTransactionLineBean> alist = new ArrayList<>();
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
                        "update in_transaction_line_table set customer_name=? " +
                        "where customer_id=?" );
                stmt.setString(1, customerObj.getName());
                stmt.setString(2, customerObj.getId());

                MyLogger.logInfo(getClass(), "updateCustomer(): executing sql (" + stmt + ")");                    
                return stmt.executeUpdate();
            }
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return 0;
        }
    }
    
    protected InTransactionLineBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            InTransactionLineBean bean = new InTransactionLineBean();
            
            bean.setId(rs.getString(1));
            bean.setItemId(rs.getString(2));
            bean.setItemName(rs.getString(3));
            bean.setItemNewName(rs.getString(4));
            bean.setItemNameBm(rs.getString(5));
            bean.setWeight(rs.getDouble(6));
            bean.setUnitPrice(rs.getDouble(7));
            bean.setBucketNo(rs.getString(8));
            bean.setCustomerId(rs.getString(9));
            bean.setCustomerName(rs.getString(10));
            bean.setInTransactionId(rs.getString(11));
            bean.setDateTime(this.timestampToLong(rs.getTimestamp(12)));
            bean.setSupplierId(rs.getString(13));
            bean.setSaving(rs.getDouble(14));
            bean.setSalesId(rs.getString(15));
            bean.setSalesLineId(rs.getString(16));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
