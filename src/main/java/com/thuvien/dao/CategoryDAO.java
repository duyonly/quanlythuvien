package com.thuvien.dao;

import com.thuvien.entity.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> getAllCategory(){
        List<Category> list =new ArrayList<>();
        String sql="SELECT * FROM categories";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                Category category=new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                list.add(category);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public boolean addCategory(Category cate){
        String sql="INSERT INTO categories (name,description) VALUES (?,?)";
        try(Connection con=DatabaseConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, cate.getName());
            ps.setString(2, cate.getDescription());
            return ps.executeUpdate()>0;
        } catch (Exception e) {
           e.printStackTrace();
           return false;
        }
    }
    public boolean updateCategory(Category cate){
        String sql="UPDATE categories SET name=?,description=? where id=?";
        try(Connection con=DatabaseConnection.getConnection();
           PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, cate.getName());
            ps.setString(2, cate.getDescription());
            ps.setInt(3, cate.getId());
            return ps.executeUpdate()>0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteCategory(int id){
        String sql="DELETE FROM categories WHERE id=?";
        try(Connection con=DatabaseConnection.getConnection();
         PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate()>0;
        } catch (Exception e) {
           e.printStackTrace();
           return false;
        }
    }
    public List<Category> getSearchCate(String keyword){
        List<Category> list= new ArrayList<>();
        String sql="SELECT * FROM categories WHERE name LIKE ?";
        try(Connection con=DatabaseConnection.getConnection();
           PreparedStatement psSearch=con.prepareStatement(sql)){
            String query="%" + keyword +"%";
            psSearch.setString(1, query);
            ResultSet rs=psSearch.executeQuery();
            while (rs.next()) {
                Category cate=new Category();
                cate.setId(rs.getInt("id"));
                cate.setName(rs.getString("name"));
                cate.setDescription(rs.getString("description"));
                list.add(cate);
            }
           }
           catch(SQLException e){
            e.printStackTrace();
           }
           return list;
    }
}
