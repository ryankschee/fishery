package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.UserBean;

public interface UserServiceIntf 
{
    public UserBean authenticate(String username, String password);
}
