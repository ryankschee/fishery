/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanworks.fishery.patch;

import com.ryanworks.fishery.server.dao.DaoFactory;
import com.ryanworks.fishery.server.dao.impl.MySQLCustomerDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierDaoImpl;
import com.ryanworks.fishery.server.service.MyServiceHandler;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.service.CustomerServiceIntf;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import com.ryanworks.fishery.shared.service.SupplierServiceIntf;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public class CalculateSummaryPatch 
    extends MyServiceHandler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        new Thread( new Runnable() {
            @Override
            public void run() {
                try 
                {
                    DaoFactory daoFactory = DaoFactory.getDAOFactory(DaoFactory.MYSQL);
                    
                    // CUSTOMER
                    MySQLCustomerDaoImpl customerDao = daoFactory.getCustomerDao();                    
                    CustomerServiceIntf customerService = ServiceLocator.getCustomerService();
                    
                    List<CustomerBean> customerList = customerDao.getAllCustomers();
                    for (CustomerBean customerObj : customerList) {
                        //if (customerObj.getId().equals("2")) {
                            customerService.updateSummaryByMonth(customerObj.getId());
                        //}
                    }
                    System.out.println("Patch Customer Completed");                    
                    
                    // SUPPLIER                                        
                    MySQLSupplierDaoImpl supplierDao = daoFactory.getSupplierDao();        
                    SupplierServiceIntf supplierService = ServiceLocator.getSupplierService();
                    
                    List<SupplierBean> supplierList = supplierDao.getAllSuppliers();
                    for (SupplierBean supplierObj : supplierList) {
                        supplierService.updateSummary(supplierObj.getId());
                        supplierService.updateSummarySaving(supplierObj.getId());
                    }
                    System.out.println("Patch Supplier Completed");
                }
                catch (Exception e) 
                {
                    MyLogger.logError(getClass(), e.getMessage());                    
                }
            }
        }).start();
    }
    
    
}
