package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.UserBean;

/**
 *
 * @author Ryan Chee
 */
public interface UserDaoIntf 
{   
    public int insertUser(UserBean bean);
    public boolean updateUser(UserBean bean);
    public boolean deleteUser(UserBean bean);
    public UserBean findSingleUserById(String id);
    public UserBean findSingleUserByUsername(String username);        
}
