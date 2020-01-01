package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.server.service.CustomerServiceHandler;
import com.ryanworks.fishery.server.service.InTransactionServiceHandler;
import com.ryanworks.fishery.server.service.ReportServiceHandler;
import com.ryanworks.fishery.server.service.SalesServiceHandler;
import com.ryanworks.fishery.server.service.SupplierServiceHandler;
import com.ryanworks.fishery.server.service.SystemDataServiceHandler;
import com.ryanworks.fishery.server.service.UserServiceHandler;

public class ServiceLocator {

    private static UserServiceIntf userService;
    private static CustomerServiceIntf customerService;
    private static SystemDataServiceIntf systemDataService;
    private static SupplierServiceIntf supplierService;
    private static InTransactionServiceIntf inTransactionService;
    private static SalesServiceIntf salesService;
    private static ReportServiceIntf reportService;
        
    public static UserServiceIntf getUserService() 
    {
        if (userService == null)
            userService = new UserServiceHandler();
        
        return userService;
    }
    
    public static CustomerServiceIntf getCustomerService() 
    {
        if (customerService == null)
            customerService = new CustomerServiceHandler();
        
        return customerService;
    }
    
    public static SystemDataServiceIntf getSystemDataService() 
    {
        if (systemDataService == null)
            systemDataService = new SystemDataServiceHandler();
        
        return systemDataService;
    }
    
    public static SupplierServiceIntf getSupplierService() 
    {
        if (supplierService == null)
            supplierService = new SupplierServiceHandler();
        
        return supplierService;
    }
    
    public static InTransactionServiceIntf getInTransactionService() 
    {
        if (inTransactionService == null)
            inTransactionService = new InTransactionServiceHandler();
        
        return inTransactionService;
    }
    
    public static SalesServiceIntf getSalesService() 
    {
        if (salesService == null)
            salesService = new SalesServiceHandler();
        
        return salesService;
    }
    
    public static ReportServiceIntf getReportService() 
    {
        if (reportService == null)
            reportService = new ReportServiceHandler();
        
        return reportService;
    }
}
