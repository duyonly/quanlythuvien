package com.thuvien.bus;

import com.thuvien.dao.CategoryDAO;
import com.thuvien.entity.Category;

import java.util.List;

public class CategoryBUS {
    private final CategoryDAO categoryDAO=new CategoryDAO();
    public List<Category> getAllCategory(){
        return categoryDAO.getAllCategory();
    }
    public boolean addCategory(Category cate){
        return categoryDAO.addCategory(cate);
    }
    public boolean updateCategory(Category cate){
        return categoryDAO.updateCategory(cate);
    }
    public boolean deleteCategory(int id){
        return categoryDAO.deleteCategory(id);
    }
    public List<Category> getSearch(String keyword){
        return categoryDAO.getSearchCate(keyword);
    }
}
