package com.thuvien.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuvien.bus.ThongKeBUS;
import com.thuvien.dto.ThongKeDTO;

@RestController
@RequestMapping("/api/thongke")
@CrossOrigin(origins = "*")
public class ThongKeController {
    private ThongKeBUS thongKeBUS=new ThongKeBUS();
    @GetMapping("/tongquan")
    public ThongKeDTO getThongKe(){
        return thongKeBUS.getThongKe();
    }
}

