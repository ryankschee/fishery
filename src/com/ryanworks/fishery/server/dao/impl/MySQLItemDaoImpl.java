package com.ryanworks.fishery.server.dao.impl;

import com.ryanworks.fishery.server.dao.MySQLConnector;
import com.ryanworks.fishery.server.dao.MySQLxDao;
import com.ryanworks.fishery.server.dao.intf.ItemDaoIntf;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLItemDaoImpl
    extends MySQLxDao
    implements ItemDaoIntf {

    @Override
    public int insertItem(ItemBean bean) 
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
                            "insert into item_table values (?,?,?,?,?,?,?)");
                    stmt.setString(1, bean.getId());
                    stmt.setString(2, bean.getName());
                    stmt.setString(3, bean.getNameBm());
                    stmt.setDouble(4, bean.getPrice());
                    stmt.setString(5, bean.getDescription());
                    stmt.setString(6, bean.getPhotoUrl());
                    stmt.setString(7, bean.getCategoryId());

                    //MyLogger.log(getClass(), "insertItem(): executing sql (" + stmt + ")");                    
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
    public boolean updateItem(ItemBean bean) 
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
                            "update item_table set name=?, name_bm=?, price=?, description=?, photo_url=?, category_id=? " +
                            "where id=?" );
                    stmt.setString(1, bean.getName());
                    stmt.setString(2, bean.getNameBm());
                    stmt.setDouble(3, bean.getPrice());
                    stmt.setString(4, bean.getDescription());
                    stmt.setString(5, bean.getPhotoUrl());
                    stmt.setString(6, bean.getCategoryId());
                    stmt.setString(7, bean.getId());

                    //MyLogger.log(getClass(), "updateItem(): executing sql (" + stmt + ")");                    
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
    public boolean deleteItem(ItemBean bean) 
    {        
        if (findSingleItemById(bean.getId())==null)
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
                            "delete from item_table where id=?");
                    stmt.setString(1, bean.getId());
                    
                    //MyLogger.log(getClass(), "deleteItem(): executing sql (" + stmt + ")");                    
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
    public ItemBean findSingleItemById(String id) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt =
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from item_table where id=?");
                stmt.setString(1, id);

                //MyLogger.log(getClass(), "findSingleItemById(): executing sql (" + stmt + ")");                
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
    public List<ItemBean> findItemsByCategoryId(String categoryId) 
    {
        try 
        {
            if(MySQLConnector.getInstance().getConnection()==null)
                return null;
            else 
            {
                PreparedStatement stmt = 
                    MySQLConnector.getInstance().getConnection().prepareStatement(
                        "select * from item_table where category_id=?");
                stmt.setString(1, categoryId);                
                
                //MyLogger.log(getClass(), "findItemsByCategoryId(): executing sql (" + stmt + ")");                
                ResultSet rs = stmt.executeQuery();

                List<ItemBean> alist = new ArrayList<>();
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
    
    protected ItemBean toBeanObject(ResultSet rs) 
    {        
        try 
        {            
            ItemBean bean = new ItemBean();
            
            bean.setId(rs.getString(1));
            bean.setName(rs.getString(2));
            bean.setNameBm(rs.getString(3));
            bean.setPrice(rs.getDouble(4));
            bean.setDescription(rs.getString(5));
            bean.setPhotoUrl(rs.getString(6));
            bean.setCategoryId(rs.getString(7));
            
            return bean;
        }
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
}
