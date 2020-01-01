package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoFactory;

/**
 * Superclass for Service Handler
 * 
 * @author Ryan
 */
public class MyServiceHandler 
{
    private DaoFactory daoFactory;

    public MyServiceHandler() 
    {
        // Get MySQL factory object
        daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
    }

    protected final DaoFactory getDaoFactoryObject() 
    {
        return this.daoFactory;
    }
}
