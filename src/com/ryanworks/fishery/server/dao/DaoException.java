package com.ryanworks.fishery.server.dao;

import com.ryanworks.fishery.shared.exception.GeneralException;

public class DaoException 
    extends GeneralException
{
    public DaoException() {}
 
    public DaoException(String message) {
        super(message);
    }
 
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public DaoException(Throwable cause) {
        super(cause);
    }
}
