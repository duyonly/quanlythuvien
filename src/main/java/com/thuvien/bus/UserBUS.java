package com.thuvien.bus;

import java.util.List;

import com.thuvien.dao.AccountDAO;
import com.thuvien.dao.UserDAO;
import com.thuvien.dto.ForgotPasswordDTO;
import com.thuvien.dto.RegisterDTO;
import com.thuvien.dto.UserViewDTO;
import com.thuvien.entity.Account;
import com.thuvien.entity.User;


public class UserBUS {
    private final UserDAO userDAO=new UserDAO();
    private final AccountDAO accountDAO=new AccountDAO();
    public List<UserViewDTO> getAllUser(){
        return userDAO.allUser();
    }
    public boolean addUser(UserViewDTO user){
        return userDAO.insertUser(user);
    }
    public boolean updateUser(UserViewDTO user){
        return userDAO.updateUser(user);
    }
    public boolean deleteUser(int id){
        return userDAO.deleteUser(id);
    }
    public List<UserViewDTO> getUser(String keyword){
        return userDAO.getUser(keyword);
    }
    public boolean register(RegisterDTO register){
        return userDAO.register(register);
    }
    public boolean forgotPassword(ForgotPasswordDTO forgot){
        return userDAO.forgotPassword(forgot);
    }
    public UserViewDTO getById(int id){
        User user=userDAO.getById(id);
        Account account=accountDAO.getById(id);
        if(user==null){
            return null;
        }
        UserViewDTO userDto=new UserViewDTO();
        userDto.setId(user.getId());
        userDto.setFullname(user.getFullname());
        userDto.setEmail(user.getEmail());
        if(account!=null){
            userDto.setUsername(account.getUsername());
            userDto.setRole(account.getRole());
            userDto.setStatus(account.getStatus());
        }
        return userDto;
    }
}
