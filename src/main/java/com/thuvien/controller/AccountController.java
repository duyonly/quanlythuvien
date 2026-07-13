package com.thuvien.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuvien.bus.AccountBUS;
import com.thuvien.dto.LoginRequest;

import com.thuvien.entity.Account;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AccountController {
    private AccountBUS accountBUS=new AccountBUS();
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        Account account=accountBUS.login(request.getUsername(), request.getPassword());
        if(account !=null){
            account.setPassword(null);
            return ResponseEntity.ok(account);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản hoặc mật khẩu không đúng, hoặc đã bị khóa");
        }
    } 
    
    

}
