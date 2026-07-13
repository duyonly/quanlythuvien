package com.thuvien.dto;

public class ThongKeDTO {
    private int tongSoSach;
    private int soSachDangMuon;
    private double tongTienPhat;
    private int tongSoDocGia;
    public ThongKeDTO(){};
    public int getTongSoSach(){
        return tongSoSach;
    }
    public void setTongSoSach(int tongSoSach){
        this.tongSoSach=tongSoSach;
    }
    public int getSoSachDangMuon() {
        return soSachDangMuon;
    }
    public void setSoSachDangMuon(int soSachDangMuon) {
        this.soSachDangMuon = soSachDangMuon;
    }
    public double getTongTienPhat() {
        return tongTienPhat;
    }
    public void setTongTienPhat(double tongTienPhat) {
        this.tongTienPhat = tongTienPhat;
    }
    public int getTongSoDocGia() {
        return tongSoDocGia;
    }
    public void setTongSoDocGia(int tongSoDocGia) {
        this.tongSoDocGia = tongSoDocGia;
    }
    
}