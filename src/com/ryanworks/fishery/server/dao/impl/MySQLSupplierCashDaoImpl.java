package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SupplierCashDaoIntf;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSupplierCashDaoImpl
    extends MySQLxDao
    implements SupplierCashDaoIntf {

    @Override
    public int insertCash(SupplierCashBean bean) 
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
                            "insert into supplier_cash_table values (?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getSupplierId());
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getCashDate()));
                    stmt.setString(4, bean.getCashDesc());
                    stmt.setDouble(5, bean.getCashAmount());

                    //MyLogger.log(getClass(), "insertCash(): executing sql (" + stmt + ")");                    
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
    public boolean updateCash(SupplierCashBean bean) 
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
                            "update supplier_cash_table set supplier_id=?, cash_date=?, cash_desc=?, cash_amount=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getSupplierId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getCashDate()));
                    stmt.setString(3, bean.getCashDesc());
                    stmt.setDouble(4, bean.getCashAmount());
                    stmt.setString(5, bean.getId());

                    //MyLogger.log(getClass(), "updateCash(): executing sql (" + stmt + ")");                    
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
    public boolean deleteCash(SupplierCashBean bean) 
    {        
        if (findSingleCashById(bean.getId())==null)
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
                            "delete from supplier_cash_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteCash(): executing sql (" + stmt + ")");                    
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
    public boolean deleteCashesBySupplier(String supplierId) 
    {        
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from supplier_cash_table where supplier_id=?");
                stmt.setString(1, supplierId);

                //MyLogger.log(getClass(), "deleteCashesBySupplierId(): executing sql (" + stmt + ")");                    
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
    public List<SupplierCashBean> getAllCashs() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cash_table");
                //MyLogger.log(getClass(), "getAllCashs(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierCashBean> alist = new ArrayList<>();
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
    public SupplierCashBean findSingleCashById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cash_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleCashById(): executing sql (" + stmt + ")");                
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
    public List<SupplierCashBean> findCashsByDateRange(long dateTimeFrom, long dateTimeTo)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cash_table where " + 
                        "cash_date between ? and ? order by cash_date asc");
                stmt.setTimestamp(1, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findCashsByDateRange(): executing sql (" + stmt + ")");      
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierCashBean> alist = new ArrayList<>();
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
    public List<SupplierCashBean> findCashsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cash_table where " + 
                        "supplier_id=? and " + 
                        "cash_date between ? and ? order by cash_date asc");
                stmt.setString(1, supplier.getId());
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(3, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findCashsByDateAndSupplier(): executing sql (" + stmt + ")");   
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierCashBean> alist = new ArrayList<>();
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
    
    protected SupplierCashBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SupplierCashBean bean = new SupplierCashBean();
            
            bean.setId(rs.getString(1));
            bean.setSupplierId(rs.getString(2));
            bean.setCashDate(this.timestampToLong(rs.getTimestamp(3)));
            bean.setCashDesc(rs.getString(4));
            bean.setCashAmount(rs.getDouble(5));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
