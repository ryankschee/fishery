package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLCustomerDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLCustomerPaymentDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLCustomerSummaryDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionLineDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesLineDaoImpl;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.CustomerPaymentBean;
import com.ryanworks.fishery.shared.bean.CustomerSummaryBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.service.CustomerServiceIntf;
import com.ryanworks.fishery.shared.util.MathUtil;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomerServiceHandler
    extends MyServiceHandler
    implements CustomerServiceIntf 
{
    @Override
    public List<CustomerBean> getAllCustomers()
    {
        try 
        {
            MySQLCustomerDaoImpl dao = this.getDaoFactoryObject().getCustomerDao();
            return dao.getAllCustomers();
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return new ArrayList();
        }
    }
    
    public List<CustomerBean> getCustomersByDate(long dateTime)
    {
        try 
        {
            MySQLCustomerDaoImpl dao = this.getDaoFactoryObject().getCustomerDao();
            return dao.findCustomersByDate(dateTime);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return new ArrayList();
        }
    }
    
    @Override
    public CustomerBean getCustomerByCustomerId(String customerId) 
    {
        try 
        {
            // Get User DAO object
            MySQLCustomerDaoImpl dao = this.getDaoFactoryObject().getCustomerDao();            
            return dao.findSingleCustomerById(customerId);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
        
    @Override
    public List<CustomerPaymentBean> getPaymentsByCustomerAndDateRange(String customerId, long startTime, long endTime)
    {
        try 
        {
            // Get User DAO object
            MySQLCustomerPaymentDaoImpl dao = this.getDaoFactoryObject().getCustomerPaymentDao();
            return dao.findPaymentsByCustomerAndDateRange(customerId, startTime, endTime);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public CustomerSummaryBean getSummaryByCustomerAndDate(String customerId, int year, int month)
    {
        try 
        {
            // Get User DAO object
            MySQLCustomerSummaryDaoImpl dao = this.getDaoFactoryObject().getCustomerSummaryDao();
            return dao.findSummaryByCustomerAndDate(customerId, year, month);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime)
    {
        try 
        {
            // Get User DAO object
            MySQLSalesDaoImpl dao = this.getDaoFactoryObject().getSalesDao();            
            return dao.getTripNoByCustomerIdAndDate(customerId, dateTime);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return 0;
        }
    }
    
    @Override
    public void saveOrUpdateCustomer(CustomerBean customer) 
    {
        try 
        {
            MySQLCustomerDaoImpl dao = this.getDaoFactoryObject().getCustomerDao();
            if (dao.findSingleCustomerById( customer.getId() ) == null)
            {
                dao.insertCustomer(customer);
            }
            else
            {
                dao.updateCustomer(customer);
                
                // Update in_transaction_line_table
                MySQLInTransactionLineDaoImpl transactionLineDao = this.getDaoFactoryObject().getInTransactionLineDao();
                transactionLineDao.updateCustomer(customer);
                
                // Update sales_table
                MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
                salesDao.updateCustomer(customer);
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void saveOrUpdatePayment(CustomerPaymentBean payment) 
    {
        try 
        {
            MySQLCustomerPaymentDaoImpl paymentDao = this.getDaoFactoryObject().getCustomerPaymentDao();
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            
            if (paymentDao.findSinglePaymentById( payment.getId() ) == null)
            {
                paymentDao.insertPayment(payment);
            }
            else
            {
                paymentDao.updatePayment(payment);
            }
            
            // Set status 'Changed' to true
            updateCustomerChangedStatus(payment.getCustomerId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void saveOrUpdateSummary(CustomerSummaryBean summary) 
    {
        try 
        {
            MySQLCustomerSummaryDaoImpl dao = this.getDaoFactoryObject().getCustomerSummaryDao();
            if (dao.findSingleSummaryById(summary.getId() ) == null)
            {
                dao.insertSummary(summary);
            }
            else
            {
                dao.updateSummary(summary);
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void deleteCustomer(CustomerBean customer) 
    {
        try 
        {
            this.getDaoFactoryObject().getCustomerPaymentDao().deletePaymentsByCustomer(customer.getId());
            this.getDaoFactoryObject().getCustomerSummaryDao().deleteSummaryByCustomer(customer.getId());
            
            MySQLCustomerDaoImpl dao = this.getDaoFactoryObject().getCustomerDao();
            dao.deleteCustomer(customer);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void deletePayment(CustomerPaymentBean payment) 
    {
        try 
        {
            MySQLCustomerPaymentDaoImpl dao = this.getDaoFactoryObject().getCustomerPaymentDao();
            dao.deletePayment(payment);
            
            // Set status 'Changed' to true
            updateCustomerChangedStatus(payment.getCustomerId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void deleteSummary(CustomerSummaryBean summary) 
    {
        try 
        {                    
            MySQLCustomerSummaryDaoImpl dao = this.getDaoFactoryObject().getCustomerSummaryDao();
            dao.deleteSummary(summary);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void updateSummaryByMonth(String customerId)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = 
                    CustomerServiceHandler.this.getDaoFactoryObject().getSalesDao();
            MySQLSalesLineDaoImpl salesLineDao = 
                    CustomerServiceHandler.this.getDaoFactoryObject().getSalesLineDao();
            MySQLCustomerPaymentDaoImpl paymentDao = 
                    CustomerServiceHandler.this.getDaoFactoryObject().getCustomerPaymentDao();
            MySQLCustomerSummaryDaoImpl summaryDao = 
                    CustomerServiceHandler.this.getDaoFactoryObject().getCustomerSummaryDao();

            // Remove all existings
            summaryDao.deleteSummaryByCustomer(customerId);

            int startingYear = 2017;
            int endingYear = Calendar.getInstance().get(Calendar.YEAR);

            double lastMonthBalance = 0.0;

            for (int year = startingYear ; year <= endingYear ; year++) {
                for (int monthIndex = 1 ; monthIndex <= 12 ; monthIndex++) {
                    Calendar startDate = Calendar.getInstance();
                    //startDate.setTimeInMillis( anyday );
                    startDate.set(Calendar.YEAR, year);
                    startDate.set(Calendar.MONTH, monthIndex - 1);
                    startDate.set(Calendar.DATE, 1);
                    startDate.set(Calendar.HOUR_OF_DAY, 0);
                    startDate.set(Calendar.MINUTE, 0);
                    startDate.set(Calendar.SECOND, 0);
                    startDate.set(Calendar.MILLISECOND, 0);

                    Calendar endDate = Calendar.getInstance();

                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, monthIndex - 1);

                    endDate.set(Calendar.DATE, YearMonth.of(year, monthIndex).lengthOfMonth());
                    // endDate.set(Calendar.DATE, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

                    endDate.set(Calendar.HOUR_OF_DAY, 22);
                    endDate.set(Calendar.MINUTE, 0);
                    endDate.set(Calendar.SECOND, 0);      
                    endDate.set(Calendar.MILLISECOND, 0);      

                    double totalSalesAmount = 0.0d;
                    double totalPaymentAmount = 0.0d;

                    List<SalesBean> salesList =
                            salesDao.findSalesByCustomerAndDateRange(
                                    customerId, startDate.getTimeInMillis(), endDate.getTimeInMillis());            
                    List<CustomerPaymentBean> paymentList = 
                            paymentDao.findPaymentsByCustomerAndDateRange(
                                    customerId, startDate.getTimeInMillis(), endDate.getTimeInMillis());
                    CustomerSummaryBean customerSummary =
                            summaryDao.findSummaryByCustomerAndDate(
                                    customerId, year, monthIndex);

                    for (SalesBean salesObj : salesList)
                    {
                        List<SalesLineBean> lineList = salesLineDao.findSalesLinesBySalesId(salesObj.getId());
                        salesObj.setLineCount(lineList.size());if (lineList.size()==0)
                            continue;

                        totalSalesAmount = totalSalesAmount + salesObj.getTotalPrice();                                
                    }

                    for (CustomerPaymentBean payment : paymentList)
                    {
                        totalPaymentAmount = totalPaymentAmount + payment.getAmount();
                    }

                    customerSummary.generateId();
                    customerSummary.setTotalSalesAmount(MathUtil.round(totalSalesAmount, 2));
                    customerSummary.setTotalPaymentAmount(MathUtil.round(totalPaymentAmount, 2));
                    customerSummary.setBalance(MathUtil.round(lastMonthBalance + totalPaymentAmount - totalSalesAmount, 2));
                    // Insert summary as new records
                    summaryDao.insertSummary(customerSummary);

                    lastMonthBalance = customerSummary.getBalance();
                }
            }

            // Set status 'Changed' to false
            updateCustomerChangedStatus(customerId, false);
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void updateCustomerBalance(String customerId)
    {
        try 
        {
            double totalSalesAmount = 0.0d;
            double totalPaymentAmount = 0.0d;

            MySQLCustomerDaoImpl customerDao = CustomerServiceHandler.this.getDaoFactoryObject().getCustomerDao();
            MySQLSalesDaoImpl salesDao = CustomerServiceHandler.this.getDaoFactoryObject().getSalesDao();
            MySQLSalesLineDaoImpl salesLineDao = CustomerServiceHandler.this.getDaoFactoryObject().getSalesLineDao();
            MySQLCustomerPaymentDaoImpl paymentDao = CustomerServiceHandler.this.getDaoFactoryObject().getCustomerPaymentDao();

            CustomerBean customer = 
                    customerDao.findSingleCustomerById(customerId);

            List<SalesBean> salesList =
                    salesDao.findSalesByCustomer( customerId );

            List<CustomerPaymentBean> paymentList = 
                    paymentDao.findPaymentsByCustomer( customerId );

            for (SalesBean sales : salesList)
            {
                double salesLineAmount = 0.0d;
                List<SalesLineBean> salesLineList = salesLineDao.findSalesLinesBySalesId(sales.getId());
                for (SalesLineBean salesLine : salesLineList)
                {
                    salesLineAmount = salesLineAmount + (salesLine.getUnitPrice() * (salesLine.getWeight() + salesLine.getAddWeight()));
                }

                sales.setTotalPrice( salesLineAmount );
                totalSalesAmount = totalSalesAmount + sales.getTotalPrice();
            }

            for (CustomerPaymentBean payment : paymentList)
            {
                totalPaymentAmount = totalPaymentAmount + payment.getAmount();
            }

            customer.setBalance( MathUtil.round(totalPaymentAmount - totalSalesAmount, 2) );
            customer.setBalanceLastUpdate( Calendar.getInstance().getTimeInMillis() );
            customer.setChanged(true);

            customerDao.updateCustomer( customer );
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
        
    private void updateCustomerChangedStatus(String customerId, boolean changed) {
        
        try 
        {
            MySQLCustomerDaoImpl customerDao = getDaoFactoryObject().getCustomerDao();
            CustomerBean customerObj = customerDao.findSingleCustomerById(customerId);
            customerObj.setChanged(changed);
            
            customerDao.updateCustomer( customerObj );
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }    
}