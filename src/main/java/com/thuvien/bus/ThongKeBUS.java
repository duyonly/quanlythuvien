package com.thuvien.bus;

import com.thuvien.dao.ThongKeDAO;
import com.thuvien.dto.ThongKeDTO;

public class ThongKeBUS {
    ThongKeDAO thongKeDAO=new ThongKeDAO();
    public ThongKeDTO getThongKe(){
        return thongKeDAO.getSoLieuTongQuan();
    }
}
