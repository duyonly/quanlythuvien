package com.thuvien.bus;

import com.thuvien.dao.AccountDAO;

import com.thuvien.entity.Account;

public class AccountBUS {
    private AccountDAO accountDAO=new AccountDAO();
    public Account login(String username,String password){
        if(username==null || password==null) return null;
        return accountDAO.login(username, password);
    }
   
}