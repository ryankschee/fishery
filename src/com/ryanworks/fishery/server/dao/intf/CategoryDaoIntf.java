package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.CategoryBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface CategoryDaoIntf 
{
    public int insertCategory(CategoryBean bean);
    public boolean updateCategory(CategoryBean bean);
    public boolean deleteCategory(CategoryBean bean);
    
    public List<CategoryBean> getAllCategories();
    public CategoryBean findSingleCategoryById(String id);      
}
