package com.thuvien.entity;

public class User {
    private int id;
    private String fullname;
    private String email;
    
    public User(){};
    public User(int id, String fullname,String email){
        this.id=id;
        this.fullname=fullname;
        this.email=email;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getFullname(){
        return fullname;
    }
    public void setFullname(String fullname){
        this.fullname=fullname;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
}
