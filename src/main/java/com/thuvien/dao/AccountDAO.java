package com.thuvien.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.thuvien.entity.Account;

public class AccountDAO {
    public Account login(String username, String password){
        String sql="SELECT * FROM accounts WHERE username=? AND password=? AND status=true";
        try(Connection con =DatabaseConnection.getConnection();
           PreparedStatement psLogin=con.prepareStatement(sql)){
            psLogin.setString(1, username);
            psLogin.setString(2, password);
            ResultSet rs=psLogin.executeQuery();
            if(rs.next()){
                Account account=new Account();
                account.setId(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setStatus(rs.getBoolean("status"));
                int uId=rs.getInt("user_id");
                account.setUserId(rs.wasNull()?null :uId);
                return account;
            }}catch(SQLException e){
                e.printStackTrace();
            }
           return null;
    }
    public Account getById(int id){
        String sql="SELECT * FROM accounts WHERE user_id=?";
        try(Connection con=DatabaseConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                Account account=new Account();
                account.setId(rs.getInt("id"));
                account.setPassword(rs.getString("password"));
                account.setUsername(rs.getString("username"));
                account.setRole(rs.getString("role"));
                account.setStatus(rs.getBoolean("status"));
                account.setUserId(rs.getInt("user_id"));
                return account;
            }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }
    }
   

