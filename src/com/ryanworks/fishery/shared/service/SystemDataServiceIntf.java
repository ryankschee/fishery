package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.CategoryBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import java.util.List;

public interface SystemDataServiceIntf 
{
    public String getNextInTransactionId();
    
    public String getNextSalesId();
    
    public List<CategoryBean> getAllCategories();
    
    public ItemBean getItemById(String itemId);
    
    public List<ItemBean> getItemsByCategoryId(String categoryId);
    
    public void saveOrUpdateItem(ItemBean item);
    
    public void deleteItem(ItemBean item);
}
