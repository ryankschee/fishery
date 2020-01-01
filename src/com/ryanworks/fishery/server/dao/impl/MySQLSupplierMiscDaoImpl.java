package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SupplierMiscDaoIntf;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierMiscBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSupplierMiscDaoImpl
    extends MySQLxDao
    implements SupplierMiscDaoIntf {

    @Override
    public int insertMisc(SupplierMiscBean bean) 
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
                            "insert into supplier_misc_table values (?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getSupplierId());
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getMiscDate()));
                    stmt.setString(4, bean.getMiscDesc());
                    stmt.setDouble(5, bean.getMiscAmount());

                    //MyLogger.log(getClass(), "insertMisc(): executing sql (" + stmt + ")");                    
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
    public boolean updateMisc(SupplierMiscBean bean) 
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
                            "update supplier_misc_table set supplier_id=?, misc_date=?, misc_desc=?, misc_amount=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getSupplierId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getMiscDate()));
                    stmt.setString(3, bean.getMiscDesc());
                    stmt.setDouble(4, bean.getMiscAmount());
                    stmt.setString(5, bean.getId());

                    //MyLogger.log(getClass(), "updateMisc(): executing sql (" + stmt + ")");                    
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
    public boolean deleteMisc(SupplierMiscBean bean) 
    {        
        if (findSingleMiscById(bean.getId())==null)
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
                            "delete from supplier_misc_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteMisc(): executing sql (" + stmt + ")");                    
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
    public boolean deleteMiscsBySupplier(String supplierId) 
    {     
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from supplier_misc_table where supplier_id=?");
                stmt.setString(1, supplierId);

                //MyLogger.log(getClass(), "deleteMisc(): executing sql (" + stmt + ")");                    
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
    public List<SupplierMiscBean> getAllMiscs() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_misc_table");
                //MyLogger.log(getClass(), "getAllMiscs(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierMiscBean> alist = new ArrayList<>();
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
    public SupplierMiscBean findSingleMiscById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_misc_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleMiscById(): executing sql (" + stmt + ")");                
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
    public List<SupplierMiscBean> findMiscsByDateRange(long dateTimeFrom, long dateTimeTo)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_misc_table where " + 
                        "misc_date between ? and ? order by misc_date asc");
                stmt.setTimestamp(1, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findMiscsByDateRange(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierMiscBean> alist = new ArrayList<>();
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
    public List<SupplierMiscBean> findMiscsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_misc_table where " + 
                        "supplier_id=? and " + 
                        "misc_date between ? and ? order by misc_date asc");
                stmt.setString(1, supplier.getId());
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(3, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findMiscsByDateAndSupplier(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierMiscBean> alist = new ArrayList<>();
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
    
    protected SupplierMiscBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SupplierMiscBean bean = new SupplierMiscBean();
            
            bean.setId(rs.getString(1));
            bean.setSupplierId(rs.getString(2));
            bean.setMiscDate(this.timestampToLong(rs.getTimestamp(3)));
            bean.setMiscDesc(rs.getString(4));
            bean.setMiscAmount(rs.getDouble(5));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
