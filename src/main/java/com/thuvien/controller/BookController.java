package com.thuvien.controller;

import com.thuvien.API.ApiResponse;
import com.thuvien.bus.BookBUS;
import com.thuvien.entity.Book;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class BookController {
    private final BookBUS bookBus=new BookBUS();
    @GetMapping
    public List<Book> getAllBook(){
        return bookBus.getAllBook();
    }
    @PostMapping
    public ResponseEntity<ApiResponse> createBook(@RequestBody Book book,@RequestHeader(value="User_Role",required = false) String role){
        if(role==null||(!role.equals("ADMIN") && !role.equals("LIBRARIAN"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false,"bạn không có quyền thêm sách"));
        }
        book.setAvailableQuantity(book.getTotalQuantity());
        boolean success=bookBus.addBook(book);
        return success?ResponseEntity.ok(new ApiResponse(true, "thêm sách thành công"))
        :ResponseEntity.badRequest().body(new ApiResponse(false, "thêm sách thất bại"));
    }
    @PutMapping 
    public ResponseEntity<ApiResponse> updateBook(@RequestBody Book book,@RequestHeader(value="User_Role",required = false) String role){
        if(role==null||(!role.equals("ADMIN")&& !role.equals("LIBRARIAN"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "bạn không có quyền thêm sách"));

        }
        boolean success=bookBus.updateBook(book);
        return success?ResponseEntity.ok(new ApiResponse(true, "cập nhật sách thành công"))
        :ResponseEntity.badRequest().body(new ApiResponse(false, "cập nhật sách thất bại"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable int id,@RequestHeader(value="User_Role",required = false) String role){
        if(role==null||(!role.equals("ADMIN") && !role.equals("LIBRARIAN"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "bạn không có quyền hạn này!"));
        }
        boolean success=bookBus.deleteBook(id);
        return success?ResponseEntity.ok(new ApiResponse(true, "bạn đã xóa thành công sách"))
        :ResponseEntity.badRequest().body(new ApiResponse(false, "bạn xóa không thành công"));
    }
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(value = "keyword", required = false) String keyword){

        if(keyword==null || keyword.trim().isEmpty()){
            return bookBus.getAllBook();
        }
        return bookBus.searchBook(keyword.trim());
    }
}
