package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.InTransactionDaoIntf;
import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MySQLInTransactionDaoImpl
    extends MySQLxDao
    implements InTransactionDaoIntf {

    @Override
    public int insertInTransaction(InTransactionBean bean) 
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
                            "insert into in_transaction_table values (?,?,?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getSupplierId());
                    stmt.setString(3, bean.getSupplierName());
                    stmt.setInt(4, bean.getSupplierTrip());
                    stmt.setTimestamp(5, new Timestamp(filterToDateOnly(bean.getDateTime())));
                    stmt.setString(6, bean.getTransactionNo());    
                    stmt.setDouble(7, bean.getTotalPrice());
                    stmt.setDouble(8, bean.getTotalBonus());
                    stmt.setDouble(9, bean.getTotalSaving());

                    //MyLogger.log(getClass(), "insertInTransaction(): executing sql (" + stmt + ")");                    
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
    public boolean updateInTransaction(InTransactionBean bean) 
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
                            "update in_transaction_table set supplier_id=?, supplier_name=?, " +
                            "supplier_trip=?, date_time=?, transaction_no=?, total_price=?, total_bonus=?, total_saving=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getSupplierId());
                    stmt.setString(2, bean.getSupplierName());
                    stmt.setInt(3, bean.getSupplierTrip());
                    stmt.setTimestamp(4, new Timestamp(filterToDateOnly(bean.getDateTime())));
                    stmt.setString(5, bean.getTransactionNo());  
                    stmt.setDouble(6, bean.getTotalPrice()); 
                    stmt.setDouble(7, bean.getTotalBonus());
                    stmt.setDouble(8, bean.getTotalSaving());
                    stmt.setString(9, bean.getId()); 

                    //MyLogger.log(getClass(), "updateInTransaction(): executing sql (" + stmt + ")");                    
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
    public boolean deleteInTransaction(InTransactionBean bean) 
    {        
        if (findSingleInTransactionById(bean.getId())==null)
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
                            "delete from in_transaction_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteInTransaction(): executing sql (" + stmt + ")");                    
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
    public InTransactionBean findSingleInTransactionById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where id=?");
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
    public List<InTransactionBean> findInTransactionsByDate(long timeInMillis) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                Calendar dayStart = Calendar.getInstance();
                dayStart.setTimeInMillis(timeInMillis);
                
                dayStart.set(Calendar.HOUR_OF_DAY, 12);
                dayStart.set(Calendar.MINUTE, 0);
                dayStart.set(Calendar.SECOND, 0);
                
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where date(date_time) = ? order by date_time desc");
                stmt.setDate(1, new Date(dayStart.getTimeInMillis()));                  
                ResultSet rs = stmt.executeQuery();

                List<InTransactionBean> alist = new ArrayList<>();
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
    public List<InTransactionBean> findInTransactionsByDateRange(long dateFrom, long dateTo) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where date(date_time) between ? and ? order by date_time desc");
                stmt.setDate(1, new Date(dateFrom));     
                stmt.setDate(2, new Date(dateTo));                
                
                ResultSet rs = stmt.executeQuery();

                List<InTransactionBean> alist = new ArrayList<>();
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
    public List<InTransactionBean> findInTransactionsBySupplier(SupplierBean supplier) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where supplier_id=?");
                stmt.setString(1, supplier.getId());               
                
                ResultSet rs = stmt.executeQuery();

                List<InTransactionBean> alist = new ArrayList<>();
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
    public List<InTransactionBean> findInTransactionsByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where supplier_id=? and date_time between ? and ? order by date_time asc");
                stmt.setString(1, supplier.getId());
                stmt.setTimestamp(2, new Timestamp(filterToDateOnly(dateFrom)));     
                stmt.setTimestamp(3, new Timestamp(filterToDateOnly(dateTo)));                
                
                ResultSet rs = stmt.executeQuery();

                List<InTransactionBean> alist = new ArrayList<>();
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
    public List<InTransactionBean> findSavingTransactionsByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from in_transaction_table where supplier_id=? and date_time between ? and ? order by date_time asc");
                stmt.setString(1, supplier.getId());
                stmt.setTimestamp(2, new Timestamp(filterToDateOnly(dateFrom)));     
                stmt.setTimestamp(3, new Timestamp(filterToDateOnly(dateTo)));                
                
                ResultSet rs = stmt.executeQuery();

                List<InTransactionBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(this.toBeanObject(rs));
                
                List<InTransactionBean> filteredList = new ArrayList();
                for (InTransactionBean bean : alist) {
                    if (bean.getTotalSaving() > 0.0d)
                        filteredList.add(bean);
                }
                
                return filteredList;
            }
        }
        catch (Exception ex) 
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());           
            return null;
        }
    }
    
    @Override
    public int getTripNoBySupplierIdAndDate(String supplierId, long dateTime)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return -1;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select count(*) from in_transaction_table where supplier_id=? and date_time=?");
                stmt.setString(1, supplierId);
                stmt.setTimestamp(2, new Timestamp(dateTime));
                
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

    protected InTransactionBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            InTransactionBean bean = new InTransactionBean();
            
            bean.setId(rs.getString(1));
            bean.setSupplierId(rs.getString(2));
            bean.setSupplierName(rs.getString(3));
            bean.setSupplierTrip(rs.getInt(4));
            bean.setDateTime(rs.getTimestamp(5).getTime());
            bean.setTransactionNo(rs.getString(6));
            bean.setTotalPrice(rs.getDouble(7));
            bean.setTotalBonus(rs.getDouble(8));
            bean.setTotalSaving(rs.getDouble(9));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
