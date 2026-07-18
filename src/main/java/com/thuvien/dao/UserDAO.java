package com.thuvien.dao;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thuvien.dto.ForgotPasswordDTO;
import com.thuvien.dto.RegisterDTO;
import com.thuvien.dto.UserViewDTO;
import com.thuvien.entity.User;

public class UserDAO {
    public List<UserViewDTO> allUser(){
         List<UserViewDTO> list=new ArrayList<>();
         String sql="SELECT u.id, a.username, u.full_name, u.email, a.role, a.status" +
             " FROM users u" +
             " JOIN accounts a ON a.user_id=u.id";
         try(Connection con=DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UserViewDTO userDto=new UserViewDTO();
                userDto.setId(rs.getInt("id"));
                userDto.setUsername(rs.getString("username"));
                userDto.setFullname(rs.getString("full_name"));
                userDto.setEmail(rs.getString("email"));
                userDto.setRole(rs.getString("role"));
                userDto.setStatus(rs.getBoolean("status"));
                list.add(userDto);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         return list;
    }
    public boolean insertUser(UserViewDTO user){
        String sqlUser="INSERT INTO users (full_name,email) VALUES(?,?) ";
        String sqlAccount="INSERT INTO accounts (username,password,role,status,user_id) VALUE(?,?,?,?,?) ";
        try (Connection con=DatabaseConnection.getConnection()){
            con.setAutoCommit(false);
         PreparedStatement psUser=con.prepareStatement(sqlUser,Statement.RETURN_GENERATED_KEYS);
         psUser.setString(1, user.getFullname());
         psUser.setString(2, user.getEmail());
         psUser.executeUpdate();
         ResultSet rsUser=psUser.getGeneratedKeys();
         int userId=0;
         if(rsUser.next()){
            userId=rsUser.getInt(1);
         }
         PreparedStatement psAccount=con.prepareStatement(sqlAccount);
         psAccount.setString(1, user.getUsername());
         psAccount.setString(2, user.getPassword());
         psAccount.setString(3, user.getRole());
         psAccount.setBoolean(4, user.getStatus());
         psAccount.setInt(5, userId);
         psAccount.executeUpdate();
         con.commit();
         return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUser(UserViewDTO user){
        String sqlAccount="UPDATE accounts SET username=?, password=?,role=?,status=? where user_id=?";
        String sql="UPDATE users SET full_name=?,email=? where id=?";
        try(Connection con=DatabaseConnection.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement psUser=con.prepareStatement(sql);
            psUser.setString(1, user.getFullname());
            psUser.setString(2, user.getEmail());
            psUser.setInt(3, user.getId());
            psUser.executeUpdate();
             
            PreparedStatement psAccount=con.prepareStatement(sqlAccount);
            psAccount.setString(1, user.getUsername());
            psAccount.setString(2, user.getPassword());
            psAccount.setString(3, user.getRole());
            psAccount.setBoolean(4, user.getStatus());
            psAccount.setInt(5, user.getId());
            psAccount.executeUpdate();
            con.commit();
            return true;
           
           }
           catch(SQLException e){
            e.printStackTrace();
            return false;
           }
    }
    public boolean deleteUser(int id){
        String sql="UPDATE accounts SET status=0 where user_id=?";
        try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
                ps.setInt(1, id);
              return ps.executeUpdate()>0;
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
    }
    public List<UserViewDTO> getUser(String keyword){
        List<UserViewDTO> list = new ArrayList<>();
        String sql="SELECT u.id, a.username, u.full_name, u.email, a.role, a.status " +
            "FROM users u " +
            "JOIN accounts a ON u.id = a.user_id " +
            "WHERE a.username LIKE ? OR u.full_name LIKE ?";
        try(Connection con=DatabaseConnection.getConnection();
         PreparedStatement psUser=con.prepareStatement(sql)){
            String query="%"+keyword+"%";
            psUser.setString(1, query);
            psUser.setString(2, query);
            ResultSet rs=psUser.executeQuery();
            while (rs.next()) {
                UserViewDTO user=new UserViewDTO();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getBoolean("status"));
                list.add(user);
            }
         }catch(SQLException e){
            e.printStackTrace();
         }
         return list;
    }
    public boolean register(RegisterDTO register){
        String sqlUser="INSERT INTO users(full_name,email) VALUES(?,?)";
        String sqlAccount="INSERT INTO accounts(username,password,role,status,user_id) VALUE(?,?,?,?,?)";
        try(Connection con=DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement psUser=con.prepareStatement(sqlUser,Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, register.getFullname());
            psUser.setString(2, register.getEmail());
            psUser.executeUpdate();
            ResultSet rs=psUser.getGeneratedKeys();
            int userId=0;
            if(rs.next()){
                userId=rs.getInt(1);
            }
            PreparedStatement psAccount=con.prepareStatement(sqlAccount);
            psAccount.setString(1, register.getUsername());
            psAccount.setString(2, register.getPassword());
            psAccount.setString(3, "MEMBER");
            psAccount.setInt(4, 1);
            psAccount.setInt(5, userId);
            psAccount.executeUpdate();
            con.commit();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean forgotPassword(ForgotPasswordDTO forgot){
        String sql="UPDATE accounts a "+
        "JOIN users u ON a.user_id=u.id "+
        "SET a.password=? " +
        "WHERE a.username=? AND u.email=? ";
        try(Connection con=DatabaseConnection.getConnection();
         PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, forgot.getNewPassword());
            ps.setString(2, forgot.getUsername());
            ps.setString(3, forgot.getEmail());
           return ps.executeUpdate()>0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public User getById(int id){
       String sql="SELECT * FROM users WHERE id=?";
       try(Connection con=DatabaseConnection.getConnection();
       PreparedStatement ps=con.prepareStatement(sql)){
        ps.setInt(1, id);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            User user=new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setFullname(rs.getString("full_name"));
            return user;
        }
       }
       catch(SQLException e){
        e.printStackTrace();
       }
       return null;
    }
}
