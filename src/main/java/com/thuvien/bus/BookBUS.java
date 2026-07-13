package com.thuvien.bus;

import com.thuvien.dao.BookDAO;
import com.thuvien.entity.Book;

import java.util.List;

public class BookBUS {
    private final BookDAO bookDAO=new BookDAO();
    public List<Book> getAllBook() {
        return bookDAO.getAllBook();
    }
    public boolean addBook(Book book){
        return bookDAO.insertBook(book);
    }
    public boolean updateBook(Book book){
        return bookDAO.updateBook(book);
    }
    public boolean deleteBook(int id){
        return bookDAO.deleteBook(id);
    }
    public List<Book> searchBook(String keyword){
        return bookDAO.searchBook(keyword);
    }
}
