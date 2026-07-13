package com.thuvien.controller;

import com.thuvien.bus.CategoryBUS;
import com.thuvien.entity.Category;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {
    private final CategoryBUS categoryBUS=new CategoryBUS();
    @GetMapping
    public List<Category> getAllCategory(){
        return categoryBUS.getAllCategory();
    }
    @PostMapping  
    public boolean addCategory(@RequestBody Category cate){
        return categoryBUS.addCategory(cate);
    }
    @PutMapping
    public boolean updateCategory(@RequestBody Category cate){
        return categoryBUS.updateCategory(cate);
    }
    @DeleteMapping("/{id}")
    public boolean deleteCategory(@PathVariable int id){
        return categoryBUS.deleteCategory(id);
    }
    @GetMapping("/search")
    public List<Category> getSearch(@RequestParam(value="keyword" , required = false) String keyword){
        if(keyword==null || keyword.isEmpty()){
            return categoryBUS.getAllCategory();
        }
        return categoryBUS.getSearch(keyword);
    }
}
