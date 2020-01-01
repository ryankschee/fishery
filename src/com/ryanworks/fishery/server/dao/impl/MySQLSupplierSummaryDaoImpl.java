package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.SupplierSummaryDaoIntf;
import com.ryanworks.fishery.shared.bean.SupplierSummaryBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MySQLSupplierSummaryDaoImpl
    extends MySQLxDao
    implements SupplierSummaryDaoIntf {

    @Override
    public int insertSummary(SupplierSummaryBean bean) 
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
                            "insert into supplier_summary_table values (?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getSupplierId());
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getFirstDay()));
                    stmt.setTimestamp(4, this.longToTimestamp(bean.getEndDay()));
                    stmt.setDouble(5, bean.getBalance());
                    stmt.setDouble(6, bean.getTotalCreditAmount());
                    stmt.setDouble(7, bean.getTotalDebitAmount());

                    //MyLogger.log(getClass(), "insertSummary(): executing sql (" + stmt + ")");                    
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
    public boolean updateSummary(SupplierSummaryBean bean) 
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
                            "update supplier_summary_table set supplier_id=?, first_day=?, end_day=?, " +
                            "balance=?, total_credit_amount=?, total_debit_amount=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getSupplierId());
                    stmt.setTimestamp(2, this.longToTimestamp(bean.getFirstDay()));
                    stmt.setTimestamp(3, this.longToTimestamp(bean.getEndDay()));
                    stmt.setDouble(4, bean.getBalance());
                    stmt.setDouble(5, bean.getTotalCreditAmount());
                    stmt.setDouble(6, bean.getTotalDebitAmount());
                    stmt.setString(7, bean.getId());

                    //MyLogger.log(getClass(), "updateSummary(): executing sql (" + stmt + ")");                    
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
    public boolean deleteSummary(SupplierSummaryBean bean) 
    {        
        if (findSingleSummaryById(bean.getId())==null)
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
                            "delete from supplier_summary_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteSummary(): executing sql (" + stmt + ")");                    
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
    public boolean deleteSummaryBySupplier(String supplierId) 
    {        
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return false;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "delete from supplier_summary_table where supplier_id=?");
                stmt.setString(1, supplierId);

                //MyLogger.log(getClass(), "deleteSummaryBySupplier(): executing sql (" + stmt + ")");                    
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
    public SupplierSummaryBean findSingleSummaryById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_summary_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleSummaryById(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();
                if (rs.next() == true)
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
    public List<SupplierSummaryBean> findSummaryBySupplier(String customerId)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_summary_table where supplier_id=?");
                stmt.setString(1, customerId);
                //MyLogger.log(getClass(), "[1] findSummaryBySupplier(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierSummaryBean> alist = new ArrayList<>();
                while (rs.next())
                    alist.add(toBeanObject(rs));
                
                //MyLogger.log(getClass(), "[2] return alist (" + alist + ", " + alist.size() + ")");                
                                
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
    public SupplierSummaryBean findSummaryBySupplierAndDate(String supplierId, long anyday)
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from supplier_summary_table where supplier_id=? and first_day < ? and end_day > ?");
                stmt.setString(1, supplierId);
                stmt.setTimestamp(2, this.longToTimestamp(anyday));
                stmt.setTimestamp(3, this.longToTimestamp(anyday));
                //MyLogger.log(getClass(), "[1] findSummaryBySupplierAndDate(): executing sql (" + stmt + ")"); 
                
                ResultSet rs = stmt.executeQuery();
                
                List<SupplierSummaryBean> alist = new ArrayList<>();
                if (rs.next())
                    return this.toBeanObject(rs);
                else
                {
                    Calendar givenDay = Calendar.getInstance();
                    givenDay.setTimeInMillis(anyday);
                    
                    int firstDate;
                    int endDate;
                    if (givenDay.get(Calendar.DAY_OF_MONTH) <= 15)
                    {
                        firstDate = 1;
                        endDate = 15;
                    }
                    else
                    {
                        firstDate = 16;
                        endDate = givenDay.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }
                    
                    SupplierSummaryBean bean = new SupplierSummaryBean();
            
                    Calendar firstDay = Calendar.getInstance();
                    firstDay.set(Calendar.YEAR, givenDay.get(Calendar.YEAR));
                    firstDay.set(Calendar.MONTH, givenDay.get(Calendar.MONTH));
                    firstDay.set(Calendar.DAY_OF_MONTH, firstDate);
                    firstDay.set(Calendar.HOUR_OF_DAY, 0);
                    firstDay.set(Calendar.MINUTE, 0);
                    firstDay.set(Calendar.SECOND, 1);
                    
                    Calendar endDay = Calendar.getInstance();
                    endDay.set(Calendar.YEAR, givenDay.get(Calendar.YEAR));
                    endDay.set(Calendar.MONTH, givenDay.get(Calendar.MONTH));
                    endDay.set(Calendar.DAY_OF_MONTH, endDate);
                    endDay.set(Calendar.HOUR_OF_DAY, 23);
                    endDay.set(Calendar.MINUTE, 59);
                    endDay.set(Calendar.SECOND, 59);
                    
                    bean.setFirstDay(firstDay.getTimeInMillis());
                    bean.setEndDay( endDay.getTimeInMillis() );
                    
                    bean.setId("");
                    bean.setSupplierId(supplierId);
                    bean.setBalance(0.0d);
                    bean.setTotalCreditAmount(0.0d);
                    bean.setTotalDebitAmount(0.0d);
                    
                    return bean;
                }
            }
        }
        catch (Exception ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());         
            return null;
        }
    }
    
    protected SupplierSummaryBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            SupplierSummaryBean bean = new SupplierSummaryBean();
            
            bean.setId(rs.getString(1));
            bean.setSupplierId(rs.getString(2));
            bean.setFirstDay(this.timestampToLong(rs.getTimestamp(3)));
            bean.setEndDay(this.timestampToLong(rs.getTimestamp(4)));
            bean.setBalance(rs.getDouble(5));
            bean.setTotalCreditAmount(rs.getDouble(6));
            bean.setTotalDebitAmount(rs.getDouble(7));
                        
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
