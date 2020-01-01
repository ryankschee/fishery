package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.UserBean;
import com.ryanworks.fishery.shared.exception.AuthorizationException;
import com.ryanworks.fishery.shared.service.ServiceLocator;

public class UserDelegate 
{
    // Singleton
    private static final UserDelegate instance = new UserDelegate();

    private UserDelegate() {}

    public static UserDelegate getInstance() 
    {
        return instance;
    }

    //--------------------------------------------------------------------------

    public UserBean authenticate(String username, String password)
        throws AuthorizationException 
    {

        UserBean user =
            ServiceLocator.getUserService().authenticate(username, password);

        if (user == null)
            throw new AuthorizationException("Invalid username or password.");
        else
            return user;
    }
}
