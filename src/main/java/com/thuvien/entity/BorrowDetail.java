package com.thuvien.entity;

import java.sql.Date;

public class BorrowDetail {
    private int id;
    private int borrowTicketId;
    private int bookId;
    private Date returnDate;
    private double fineAmount;
    private String note;
    public BorrowDetail(){}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getBorrowTicketId() {
        return borrowTicketId;
    }
    public void setBorrowTicketId(int borrowTicketId) {
        this.borrowTicketId = borrowTicketId;
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public Date getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    public double getFineAmount() {
        return fineAmount;
    }
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    };
}