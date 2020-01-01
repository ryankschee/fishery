package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface CustomerDaoIntf 
{   
    public int insertCustomer(CustomerBean bean);
    public boolean updateCustomer(CustomerBean bean);
    public boolean deleteCustomer(CustomerBean bean);
    
    public List<CustomerBean> getAllCustomers();
    public CustomerBean findSingleCustomerById(String id);      
    public List<CustomerBean> findCustomersByDate(long dateTime);  
}
