package com.ryanworks.fishery.server.dao;
 
import com.ryanworks.fishery.shared.util.MyLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class MySQLDaoFactory
    extends DaoFactory
{         
    protected void close(Connection connection)
        throws DaoException
    {
        try {
            if (connection != null)
                connection.close();
        }
        catch (SQLException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            throw new DaoException(e);
        }
    }
 
    protected void close(PreparedStatement preparedStatement) 
        throws DaoException
    {
        try {
            if (preparedStatement != null)
                preparedStatement.close();
        }
        catch (SQLException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            throw new DaoException(e);
        }
    }
 
    protected void close(ResultSet resultSet) 
        throws DaoException
    {
        try {
            if (resultSet != null)
                resultSet.close();
        }
        catch (SQLException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            throw new DaoException(e);
        }
    }
 
    protected void close(Statement statement) 
        throws DaoException
    {
        try {
            if (statement != null)
                statement.close();
        }
        catch (SQLException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            throw new DaoException(e);
        }
    }
 }
