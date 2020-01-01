package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SupplierDaoIntf;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLSupplierDaoImpl
    extends MySQLxDao
    implements SupplierDaoIntf {

    @Override
    public int insertSupplier(SupplierBean bean) 
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
                            "insert into supplier_table values (?,?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getName());
                    stmt.setString(3, bean.getShipNumber());
                    stmt.setDouble(4, bean.getPercentage());
                    stmt.setString(5, bean.getNotes());
                    stmt.setInt(6, bean.isFrequent()?1:0);
                    stmt.setInt(7, bean.isSavingAccount()?1:0);
                    stmt.setBoolean(8, bean.isChanged());
                    
                    //MyLogger.log(getClass(), "insertSupplier(): executing sql (" + stmt + ")");                    
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
    public boolean updateSupplier(SupplierBean bean) 
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
                            "update supplier_table set name=?, ship_number=?, percentage=?, notes=?, frequent=?, saving_account=?, changed=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getName());
                    stmt.setString(2, bean.getShipNumber());
                    stmt.setDouble(3, bean.getPercentage());
                    stmt.setString(4, bean.getNotes());
                    stmt.setInt(5, bean.isFrequent()?1:0);
                    stmt.setInt(6, bean.isSavingAccount()?1:0);
                    stmt.setBoolean(7, bean.isChanged());
                    stmt.setString(8, bean.getId());

                    //MyLogger.log(getClass(), "updateSupplier(): executing sql (" + stmt + ")");                    
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
    public boolean deleteSupplier(SupplierBean bean) 
    {        
        if (findSingleSupplierById(bean.getId())==null)
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
                            "delete from supplier_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteSupplier(): executing sql (" + stmt + ")");                    
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
    public List<SupplierBean> getAllSuppliers() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_table");
                //MyLogger.log(getClass(), "getAllSuppliers(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierBean> alist = new ArrayList<>();
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
    public List<SupplierBean> getFrequentSuppliers() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_table where frequent=1");
                //MyLogger.log(getClass(), "getFrequentSuppliers(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierBean> alist = new ArrayList<>();
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
    public List<SupplierBean> getNonFrequentSuppliers() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_table where frequent=0");
                //MyLogger.log(getClass(), "getNonFrequentSuppliers(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierBean> alist = new ArrayList<>();
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
    public SupplierBean findSingleSupplierById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleSupplierById(): executing sql (" + stmt + ")");                
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
    public List<SupplierBean> findSuppliersByDate(long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where date_time=?");
                stmt.setTimestamp(1, new Timestamp(dateTime));
                //MyLogger.log(getClass(), "[1] findSuppliersByDate(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                HashMap<String, SupplierBean> map = new HashMap();
                while (rs.next()) {
                    
                    PreparedStatement stmt1 =
                        MySQLConnector.getInstance().getConnection().prepareStatement(
                            "select * from supplier_table where id=?" );
                    stmt1.setString(1, rs.getString("supplier_id"));
                    //MyLogger.log(getClass(), "[2] findSuppliersByDate(): executing sql (" + stmt1 + ")"); 
                    
                    ResultSet rs1 = stmt1.executeQuery();
                    
                    if (rs1.next()) 
                    {
                        SupplierBean supplier = this.toBeanObject(rs1);
                        map.put(supplier.getId(), supplier);
                    }
                }
                
                List<SupplierBean> alist = new ArrayList<>();
                alist.addAll(map.values());                
                                
                return alist;
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return new ArrayList();
        }
    }
    
    protected SupplierBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SupplierBean bean = new SupplierBean();
            
            bean.setId(rs.getString(1));
            bean.setName(rs.getString(2));
            bean.setShipNumber(rs.getString(3));
            bean.setPercentage(rs.getDouble(4));
            bean.setNotes(rs.getString(5));
            bean.setFrequent(rs.getInt(6)==1);
            bean.setSavingAccount(rs.getInt(7)==1);
            bean.setChanged(rs.getBoolean(8));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
