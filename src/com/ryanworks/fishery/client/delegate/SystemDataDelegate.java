package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.CategoryBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import java.util.List;
import javax.swing.SwingWorker;

public class SystemDataDelegate 
{
    // Singleton
    private static final SystemDataDelegate instance = new SystemDataDelegate();

    private SystemDataDelegate() {}

    public static SystemDataDelegate getInstance() 
    {
        return instance;
    }

    //--------------------------------------------------------------------------

    public String getNextInTransactionId()
    {
        return ServiceLocator.getSystemDataService().getNextInTransactionId();
    }
    
    public String getNextSalesId()
    {
        return ServiceLocator.getSystemDataService().getNextSalesId();
    }
    
    public List<CategoryBean> getAllCategories()
    {
        return ServiceLocator.getSystemDataService().getAllCategories();
    }
    
    public List<ItemBean> getItemsByCategoryId(String categoryId)
    {
        return ServiceLocator.getSystemDataService().getItemsByCategoryId( categoryId );
    }
    
    public ItemBean getItemById(String itemId)
    {
        return ServiceLocator.getSystemDataService().getItemById( itemId );
    }
    
    public void saveOrUpdateItem(ItemBean item)
    {
        ServiceLocator.getSystemDataService().saveOrUpdateItem( item );
    }
    
    public void saveOrUpdateItemAsync(ItemBean item)
    {
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {
                ServiceLocator.getSystemDataService().saveOrUpdateItem( item );
                return null;
            }
            @Override
            protected void done() {
                //dialog.dispose();//close the modal dialog
            }
        };
        swingWorker.execute();
    }
    
    public void deleteItem(ItemBean item)
    {
        ServiceLocator.getSystemDataService().deleteItem( item );
    }
}
