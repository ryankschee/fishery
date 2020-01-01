package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SystemBean;

/**
 *
 * @author Ryan Chee
 */
public interface SystemDaoIntf 
{
    public boolean updateSystem(SystemBean bean);
    
    public SystemBean getSystemBean(); 
}
