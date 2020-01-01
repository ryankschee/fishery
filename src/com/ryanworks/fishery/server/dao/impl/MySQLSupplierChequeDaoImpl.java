package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SupplierChequeDaoIntf;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierChequeBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSupplierChequeDaoImpl
    extends MySQLxDao
    implements SupplierChequeDaoIntf {

    @Override
    public int insertCheque(SupplierChequeBean bean) 
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
                            "insert into supplier_cheque_table values (?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getSupplierId());
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getChequeDate()));
                    stmt.setString(4, bean.getChequeNo());
                    stmt.setDouble(5, bean.getChequeAmount());

                    //MyLogger.log(getClass(), "insertCheque(): executing sql (" + stmt + ")");                    
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
    public boolean updateCheque(SupplierChequeBean bean) 
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
                            "update supplier_cheque_table set supplier_id=?, cheque_date=?, cheque_no=?, cheque_amount=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getSupplierId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getChequeDate()));
                    stmt.setString(3, bean.getChequeNo());
                    stmt.setDouble(4, bean.getChequeAmount());
                    stmt.setString(5, bean.getId());

                    //MyLogger.log(getClass(), "updateCheque(): executing sql (" + stmt + ")");                    
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
    public boolean deleteCheque(SupplierChequeBean bean) 
    {        
        if (findSingleChequeById(bean.getId())==null)
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
                            "delete from supplier_cheque_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteCheque(): executing sql (" + stmt + ")");                    
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
    public boolean deleteChequesBySupplier(String supplierId) 
    {        
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from supplier_cheque_table where supplier_id=?");
                stmt.setString(1, supplierId);

                //MyLogger.log(getClass(), "deleteChequesBySupplier(): executing sql (" + stmt + ")");                    
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
    public List<SupplierChequeBean> getAllCheques() 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cheque_table");
                //MyLogger.log(getClass(), "getAllCheques(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierChequeBean> alist = new ArrayList<>();
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
    public SupplierChequeBean findSingleChequeById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cheque_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleChequeById(): executing sql (" + stmt + ")");                
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
    public List<SupplierChequeBean> findChequesByDateRange(long dateTimeFrom, long dateTimeTo)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cheque_table where " + 
                        "cheque_date between ? and ? order by cheque_date asc");
                stmt.setTimestamp(1, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findChequesByDateRange(): executing sql (" + stmt + ")");   
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierChequeBean> alist = new ArrayList<>();
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
    public List<SupplierChequeBean> findChequesByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_cheque_table where " + 
                        "supplier_id=? and " + 
                        "cheque_date between ? and ? order by cheque_date asc");
                stmt.setString(1, supplier.getId());
                stmt.setTimestamp(2, this.longToTimestamp(dateTimeFrom));
                stmt.setTimestamp(3, this.longToTimestamp(dateTimeTo));
                //MyLogger.log(getClass(), "findChequesByDateAndSupplier(): executing sql (" + stmt + ")");   
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierChequeBean> alist = new ArrayList<>();
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
    
    protected SupplierChequeBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SupplierChequeBean bean = new SupplierChequeBean();
            
            bean.setId(rs.getString(1));
            bean.setSupplierId(rs.getString(2));
            bean.setChequeDate(this.timestampToLong(rs.getTimestamp(3)));
            bean.setChequeNo(rs.getString(4));
            bean.setChequeAmount(rs.getDouble(5));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
