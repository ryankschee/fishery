package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.CustomerPaymentBean;
import com.ryanworks.fishery.shared.bean.CustomerSummaryBean;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Ryan
 */
public class CustomerDelegate 
{
    // Singleton
    private static final CustomerDelegate instance = new CustomerDelegate();

    private CustomerDelegate() {}

    public static CustomerDelegate getInstance() 
    {
        return instance;
    }

    //--------------------------------------------------------------------------

    public CustomerBean getCustomerByCustomerId(String customerId)
    {
        return ServiceLocator.getCustomerService().getCustomerByCustomerId(customerId);
    }
    
    public List<CustomerBean> getAllCustomers()
    {
        return ServiceLocator.getCustomerService().getAllCustomers();
    }
    
    public List<CustomerBean> getCustomersByDate(long dateTime)
    {
        return ServiceLocator.getCustomerService().getCustomersByDate(dateTime);    
    }
    
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime)
    {
        return ServiceLocator.getCustomerService().getTripNoByCustomerIdAndDate(customerId, dateTime);
    }
    
    public List<CustomerPaymentBean> getPaymentsByCustomerAndDateRange(String customerId, long startTime, long endTime)
    {
        return ServiceLocator.getCustomerService().getPaymentsByCustomerAndDateRange(customerId, startTime, endTime);
    }
    
    public CustomerSummaryBean getSummaryByCustomerAndDate(String customerId, int year, int month)
    {
        return ServiceLocator.getCustomerService().getSummaryByCustomerAndDate(customerId, year, month);
    }
    
    public void saveOrUpdateCustomer(CustomerBean customer)
    {
        ServiceLocator.getCustomerService().saveOrUpdateCustomer( customer );
    }
    
    public void saveOrUpdatePayment(CustomerPaymentBean payment)
    {
        ServiceLocator.getCustomerService().saveOrUpdatePayment( payment );
    }
    
    public void saveOrUpdateSummary(CustomerSummaryBean summary)
    {
        ServiceLocator.getCustomerService().saveOrUpdateSummary(summary );
    }
    
    public void updateCustomerBalance(String customerId)
    {
        ServiceLocator.getCustomerService().updateCustomerBalance( customerId );
    }
    
    public void deleteCustomer(CustomerBean customer)
    {
        ServiceLocator.getCustomerService().deleteCustomer( customer );
    }
    
    public void deletePayment(CustomerPaymentBean payment)
    {
        ServiceLocator.getCustomerService().deletePayment( payment );
    }
    
    public void deleteSummary(CustomerSummaryBean summary)
    {
        ServiceLocator.getCustomerService().deleteSummary( summary );
    }
    
    public void updateSummaryByMonth(String customerId)
    {
        ServiceLocator.getCustomerService().updateSummaryByMonth(customerId);
    }
}
