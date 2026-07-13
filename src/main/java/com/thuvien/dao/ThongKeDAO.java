package com.thuvien.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.thuvien.dto.ThongKeDTO;

public class ThongKeDAO {
    public ThongKeDTO getSoLieuTongQuan(){
        ThongKeDTO dto=new ThongKeDTO();
        //sách trong kho
        String sqlSach="SELECT COALESCE(SUM(available_quantity),0) AS tong_sach FROM books ";
        //tổng số sách đang đc mượn
        String sqlDangMuon="SELECT COALESCE(SUM(d.quantity),0) AS dang_muon FROM borrow_details d "+
        "JOIN borrow_tickets t ON t.id=d.ticket_id WHERE t.status ='BORROWING'";
        String sqlTienPhat="SELECT COALESCE(SUM(d.fine_amount),0) AS tong_phat FROM borrow_details d";
        String sqlDocGia="SELECT COUNT(*) AS tong_user FROM accounts WHERE role='MEMBER'";
        try(Connection con =DatabaseConnection.getConnection()) {
            try(PreparedStatement psSach=con.prepareStatement(sqlSach);
              ResultSet rs=psSach.executeQuery()){
                if(rs.next()){
                    dto.setTongSoSach(rs.getInt("tong_sach"));
                }
            }
            try(PreparedStatement psDangMuon=con.prepareStatement(sqlDangMuon);
               ResultSet rs=psDangMuon.executeQuery()){
                if(rs.next()){
                    dto.setSoSachDangMuon(rs.getInt("dang_muon"));
                }
               }
               try(PreparedStatement psTienPhat=con.prepareStatement(sqlTienPhat);
                ResultSet rs=psTienPhat.executeQuery()){
                    if(rs.next()){
                        dto.setTongTienPhat(rs.getDouble("tong_phat"));
                    }
                }
                try(PreparedStatement psDocGia=con.prepareStatement(sqlDocGia);
                  ResultSet rs=psDocGia.executeQuery()){
                    if(rs.next()){
                        dto.setTongSoDocGia(rs.getInt("tong_user"));
                    }
                  }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }
}
