package com.thuvien.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

import com.thuvien.bus.UserBUS;
import com.thuvien.dto.ForgotPasswordDTO;
import com.thuvien.dto.RegisterDTO;
import com.thuvien.dto.UserViewDTO;


@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    private final UserBUS userBUS=new UserBUS();
    @GetMapping
    public List<UserViewDTO> getAllUsers(){
        return userBUS.getAllUser();
    }
    @PostMapping  
    public boolean addUser(@RequestBody UserViewDTO user){
        return userBUS.addUser(user);
    }
    @PutMapping  
    public boolean updateUser(@RequestBody UserViewDTO user){
        return userBUS.updateUser(user);
    }
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable int id){
        return userBUS.deleteUser(id);
    }
    @GetMapping("/search")
    public List<UserViewDTO> getUser(@RequestParam(value= "keyword",required = false) String keyword){
        if(keyword==null || keyword.isEmpty()){
            return getAllUsers();
        }
        return userBUS.getUser(keyword);
    }
    @PostMapping("/register")
    public boolean register(@RequestBody RegisterDTO register) {
        return userBUS.register(register);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgot) {
       boolean result=userBUS.forgotPassword(forgot);
       if(result){
        return ResponseEntity.ok("đổi mật khẩu thành công");
       }
       else
        return ResponseEntity.badRequest().body("sai username hoặc email rồi!");
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserViewDTO> getById(@PathVariable int id){
        UserViewDTO user=userBUS.getById(id);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
}
