package com.thuvien.bus;

import java.time.LocalDate;
import java.util.List;

import com.thuvien.dao.BorrowDAO;
import com.thuvien.dto.BorrowViewDTO;

public class BorrowBUS {
    private BorrowDAO borrowDAO=new BorrowDAO();

    public List<BorrowViewDTO> getAllBorrows(){
        return borrowDAO.getAllBorrows();
    }
    public String addBorrow(int userId,int bookId,int quantity,LocalDate  borrowDate, LocalDate  dueDate){
        //kiểm tra số lượng sách trong kho
        if(!borrowDAO.userExists(userId)){
            return "thất bại: id người dùng không tồn tại!"+userId;
        }
        int soLuong=borrowDAO.getAvailableQuantity(bookId);
        if(soLuong==-1){
            return "không tìm thấy cuốn sách có thông tin này";
        }
        if(soLuong<=0){
            return "sách này hiện đã hết trong kho";
        }
        boolean result=borrowDAO.insertBorrow(userId, bookId,quantity,borrowDate, dueDate);     
            if(result){
                return "đã thêm phiếu mượn thành công";
            }                                                                                                                                   
            else{
                return "tạo phiếu mượn thất bại do sự cố hệ thống";
        }
    }
    public String returnBook(int detailId){
        boolean ketQua=borrowDAO.returnBook(detailId);
        if(ketQua){
            return "Thành công: Đã cập nhật ngày trả, tiền phạt và hoàn kho sách!";
        }
        else{
            return "Lỗi: trả sách thất bại";
        }
    }
    public String updateBorrow(int detailId,int userId,int quantity, int bookId, LocalDate dueDate,String status){
        if(!borrowDAO.userExists(userId)){
            return "người dùng không tồn tại";
        }
        boolean success=borrowDAO.updateBorrow(detailId, userId,quantity, bookId, dueDate, status);
        if(success){
            return "cập nhật phiếu mượn thành công";
        }
        else{
            return "cập nhật thất bại";
        }
    }
    public List<BorrowViewDTO> getBorrow(String keyword){
        return borrowDAO.getBorrow(keyword);
    }
}