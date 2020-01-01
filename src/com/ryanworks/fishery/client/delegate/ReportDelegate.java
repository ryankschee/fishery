package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.ReportStockInBean;
import com.ryanworks.fishery.shared.bean.ReportStockOutBean;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import java.util.List;

public class ReportDelegate 
{
    // Singleton
    private static final ReportDelegate instance = new ReportDelegate();

    private ReportDelegate() {}

    public static ReportDelegate getInstance() 
    {
        return instance;
    }

    //--------------------------------------------------------------------------

    public List<ReportStockInBean> getStockInByDateAndItem(long timeInMillis, List<ItemBean> itemList)
    {
        return ServiceLocator.getReportService().getStockInByDateAndItem(timeInMillis, itemList);
    }
        
    public List<ReportStockOutBean> getStockOutByDateAndItem(long timeInMillis, List<ItemBean> itemList)
    {
        return ServiceLocator.getReportService().getStockOutByDateAndItem(timeInMillis, itemList);
    }
}
