package com.thuvien.dao;

import com.thuvien.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> getAllBook() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setAuthor(rs.getString("author"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setAvailableQuantity(rs.getInt("available_quantity"));
                book.setTotalQuantity(rs.getInt("total_quantity"));
                list.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  list;
    }
    public boolean insertBook(Book book){
        String sql="INSERT INTO books (title,author,isbn,total_quantity,available_quantity,category_id) VALUES (?, ?, ?, ?, ?,?)";
        try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
                ps.setString(1,book.getTitle());
                ps.setString(2,book.getAuthor());
                ps.setString(3,book.getIsbn());
                ps.setInt(4,book.getTotalQuantity());
                ps.setInt(5,book.getTotalQuantity());
                ps.setInt(6,book.getCategoryId());
                return ps.executeUpdate()>0;}
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public boolean updateBook(Book book){
            String sql="UPDATE books SET title=?, author=?, isbn=?, total_quantity=?,available_quantity=?,category_id=? WHERE id=?";
            try(Connection con=DatabaseConnection.getConnection();
                PreparedStatement ps=con.prepareStatement(sql)){
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    ps.setString(3, book.getIsbn());
                    ps.setInt(4, book.getTotalQuantity());
                    ps.setInt(5, book.getAvailableQuantity());
                    ps.setInt(6,book.getCategoryId());
                    ps.setInt(7,book.getId());
                    return ps.executeUpdate()>0;
                }
                catch(SQLException e){
                    e.printStackTrace();
                    return false;
                }
        }
    public boolean deleteBook(int id){
        String sql="DELETE FROM books where id=?";
        try(Connection conn=DatabaseConnection.getConnection();
    PreparedStatement ps=conn.prepareStatement(sql)){
        ps.setInt(1, id);
        return ps.executeUpdate()>0;
    }
    catch(SQLException e){
        e.printStackTrace();
        return false;
    }
    }
    public List<Book> searchBook(String keyword){
        List<Book> list=new ArrayList<>();
        String sql="SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? ";
        try(Connection con =DatabaseConnection.getConnection();
            PreparedStatement psSearch=con.prepareStatement(sql)){
                
               String query="%"+ keyword + "%";
               psSearch.setString(1, query);
               psSearch.setString(2, query);
               psSearch.setString(3, query);
               ResultSet rs=psSearch.executeQuery();
               while (rs.next()) {
                Book book=new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setTotalQuantity(rs.getInt("total_quantity"));
                book.setAvailableQuantity(rs.getInt("available_quantity"));
                book.setCategoryId(rs.getInt("category_id"));
                list.add(book);
               }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return list;
    }
}
