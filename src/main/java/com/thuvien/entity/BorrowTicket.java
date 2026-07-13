package com.thuvien.entity;

import java.sql.Date;

public class BorrowTicket {
    private int id;
    private int userid;
    private Date borrowDate;
    private Date dueDate;
    private String status;
    public BorrowTicket(){};
    public BorrowTicket(int id, int userid,Date borrowDate,Date dueDate,String status){
        this.id=id;
        this.userid=userid;
        this.borrowDate=borrowDate;
        this.dueDate=dueDate;
        this.status=status;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }
    public Date getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
