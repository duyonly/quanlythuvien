package com.thuvien.controller;


import java.time.LocalDate;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuvien.API.ApiResponse;
import com.thuvien.bus.BorrowBUS;

import com.thuvien.dto.BorrowViewDTO;
@RestController
@RequestMapping("/api/borrows")
@CrossOrigin("*")
public class BorrowController {
    private BorrowBUS borrowBUS=new BorrowBUS();
    
    @GetMapping
    public List<BorrowViewDTO> getAllBorrows(){
        return borrowBUS.getAllBorrows();
    }
    @PostMapping
    public ResponseEntity<ApiResponse> addBorrow(@RequestBody Map<String,Object> phieuMuon){
        if (phieuMuon.get("dueDate") == null || phieuMuon.get("dueDate").toString().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,"Thất bại: Vui lòng nhập hoặc chọn hạn trả sách!"));
        }
        if (phieuMuon.get("userId") == null || phieuMuon.get("bookId") == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Thất bại: Thiếu thông tin Mã người dùng hoặc Mã sách!"));
        }
        try{
        int userId=Integer.parseInt(phieuMuon.get("userId").toString());
        int bookId=Integer.parseInt(phieuMuon.get("bookId").toString());
        int quantity=Integer.parseInt(phieuMuon.get("quantity").toString());
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate=  LocalDate.parse(phieuMuon.get("dueDate").toString());
        String result= borrowBUS.addBorrow(userId, bookId,quantity,borrowDate, dueDate);
        if(result.contains("thành công")){
            return ResponseEntity.ok(new ApiResponse(true, result));
        }
        else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, result));
        }
        }
        catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Thất bại: Mã người dùng hoặc mã sách không hợp lệ!"));
        } catch (java.time.format.DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Thất bại: Định dạng ngày trả không đúng (yyyy-MM-dd)!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Thất bại: Lỗi hệ thống!"));
        }
            
    }
    //xử lý phiếu tra
    @PostMapping("/{detailId}/return")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable int detailId){
        try{
            String result = borrowBUS.returnBook(detailId);
            if(result.toLowerCase().contains("thành công")){
                return  ResponseEntity.ok(new ApiResponse(true, result));
            }else
            {
                return ResponseEntity.badRequest().body(new ApiResponse(false, result));
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "thất bại: lỗi xẩy ra trong quá trình trả sách"));
        }
    }
    @PutMapping  
    public ResponseEntity<ApiResponse> updateBorrow(@RequestBody Map<String,Object> capNhat){
        try{                                                                                                                                                                  
            int detailId=Integer.parseInt(capNhat.get("id").toString());
            int userId=Integer.parseInt(capNhat.get("userId").toString());
            int bookId=Integer.parseInt(capNhat.get("bookId").toString());
            int quantity=Integer.parseInt(capNhat.get("quantity").toString());
            LocalDate dueDate=LocalDate.parse(capNhat.get("dueDate").toString());
            String status=capNhat.get("status").toString();
            String ketQua=borrowBUS.updateBorrow(detailId, userId, bookId, quantity,dueDate, status);
            if (ketQua.contains("thành công")) {
                return ResponseEntity.ok(new ApiResponse(true, ketQua));
            }                                                           
            else{
                return ResponseEntity.badRequest().body(new ApiResponse(false,   "Lỗi cập nhật phiếu mượn!"));
            }                                                                                  
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(false, "thất bại: lỗi xẩy ra trong quá trình trả sách"));
        }
    }
    @GetMapping("/search")
    public List<BorrowViewDTO> getBorrow(@RequestParam(value="keyword", required = false)String keyword){
        if(keyword==null || keyword.isEmpty()){
            return borrowBUS.getAllBorrows();
        }
        return borrowBUS.getBorrow(keyword);
    }
}
