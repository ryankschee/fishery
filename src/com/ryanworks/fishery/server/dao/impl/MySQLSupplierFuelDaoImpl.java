package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SupplierFuelDaoIntf;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierFuelBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSupplierFuelDaoImpl
    extends MySQLxDao
    implements SupplierFuelDaoIntf {

    @Override
    public int insertFuel(SupplierFuelBean bean) 
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
                            "insert into supplier_fuel_table values (?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getSupplierId());
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getFuelDate()));
                    stmt.setDouble(4, bean.getFuelQuantity());
                    stmt.setDouble(5, bean.getFuelUnitPrice());
                    stmt.setDouble(6, bean.getFuelTotalPrice());

                    //MyLogger.log(getClass(), "insertFuel(): executing sql (" + stmt + ")");                    
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
    public boolean updateFuel(SupplierFuelBean bean) 
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
                            "update supplier_fuel_table set supplier_id=?, fuel_date=?, " + 
                            "fuel_quantity=?, fuel_unit_price=?, fuel_total_price=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getSupplierId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getFuelDate()));
                    stmt.setDouble(3, bean.getFuelQuantity());
                    stmt.setDouble(4, bean.getFuelUnitPrice());
                    stmt.setDouble(5, bean.getFuelTotalPrice());
                    stmt.setString(6, bean.getId());

                    //MyLogger.log(getClass(), "updateFuel(): executing sql (" + stmt + ")");                    
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
    public boolean deleteFuel(SupplierFuelBean bean) 
    {        
        if (findSingleFuelById(bean.getId())==null)
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
                            "delete from supplier_fuel_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteFuel(): executing sql (" + stmt + ")");                    
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
    public boolean deleteFuelsBySupplier(String supplierId) 
    {        
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from supplier_fuel_table where supplier_id=?");
                stmt.setString(1, supplierId);

                //MyLogger.log(getClass(), "deleteFuel(): executing sql (" + stmt + ")");                    
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
    public List<SupplierFuelBean> getAllFuels() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_fuel_table");
                //MyLogger.log(getClass(), "getAllFuels(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierFuelBean> alist = new ArrayList<>();
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
    public SupplierFuelBean findSingleFuelById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_fuel_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleFuelById(): executing sql (" + stmt + ")");                
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
    public List<SupplierFuelBean> findFuelsByDateRange(long dateTimeFrom, long dateTimeTo)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_fuel_table where " + 
                        "fuel_date between ? and ? order by fuel_date asc");
                stmt.setTimestamp(1, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findFuelsByDateRange(): executing sql (" + stmt + ")");  
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierFuelBean> alist = new ArrayList<>();
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
    public List<SupplierFuelBean> findFuelsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_fuel_table where " + 
                        "supplier_id=? and " + 
                        "fuel_date between ? and ? order by fuel_date asc");
                stmt.setString(1, supplier.getId());
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(3, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findFuelsByDateAndSupplier(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierFuelBean> alist = new ArrayList<>();
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
    
    protected SupplierFuelBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SupplierFuelBean bean = new SupplierFuelBean();
            
            bean.setId(rs.getString(1));
            bean.setSupplierId(rs.getString(2));
            bean.setFuelDate(this.timestampToLong(rs.getTimestamp(3)));
            bean.setFuelQuantity(rs.getDouble(4));
            bean.setFuelUnitPrice(rs.getDouble(5));
            bean.setFuelTotalPrice(rs.getDouble(6));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
