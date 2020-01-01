package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.ItemBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface ItemDaoIntf 
{
    public int insertItem(ItemBean bean);
    public boolean updateItem(ItemBean bean);
    public boolean deleteItem(ItemBean bean);
    public ItemBean findSingleItemById(String id);
    public List<ItemBean> findItemsByCategoryId(String categoryId);
}
