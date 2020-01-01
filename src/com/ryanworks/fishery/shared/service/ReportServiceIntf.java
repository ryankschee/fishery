package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.ReportStockInBean;
import com.ryanworks.fishery.shared.bean.ReportStockOutBean;
import java.util.List;

public interface ReportServiceIntf 
{
    public List<ReportStockInBean> getStockInByDateAndItem(long timeInMillis, List<ItemBean> itemList);
    
    public List<ReportStockOutBean> getStockOutByDateAndItem(long timeInMillis, List<ItemBean> itemList);
}
