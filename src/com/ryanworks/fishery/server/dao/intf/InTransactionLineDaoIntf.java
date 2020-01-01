package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface InTransactionLineDaoIntf 
{
    public int insertInTransactionLine(InTransactionLineBean bean);
    public boolean updateInTransactionLine(InTransactionLineBean bean);
    public boolean deleteInTransactionLine(InTransactionLineBean bean);
    
    public InTransactionLineBean findSingleInTransactionLineById(String id);
    public InTransactionLineBean findSingleInTransactionLineBySalesLineId(String salesLinId);
    public List<InTransactionLineBean> findInTransactionLinesByInTransactionId(String inTransactionId);
    public List<InTransactionLineBean> findInTransactionLinesByItemAndDate(ItemBean item, long dateTime);
    
    public int updateCustomer(CustomerBean customerObj);
    public int updateItemName(ItemBean itemObj);
}
