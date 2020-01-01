package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.CustomerPaymentBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface CustomerPaymentDaoIntf 
{   
    public int insertPayment(CustomerPaymentBean bean);
    public boolean updatePayment(CustomerPaymentBean bean);
    public boolean deletePayment(CustomerPaymentBean bean);
    
    public CustomerPaymentBean findSinglePaymentById(String id);      
    public List<CustomerPaymentBean> findPaymentsByCustomer(String customerId);
    public List<CustomerPaymentBean> findPaymentsByCustomerAndDateRange(String customerId, long startDate, long endDate);
    public List<CustomerPaymentBean> findPaymentsByCustomerBeforeDate(String customerId, long anyday);
    
    public boolean deletePaymentsByCustomer(String customerId);
}