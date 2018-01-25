package com.example.wenda.service;

import org.springframework.stereotype.Service;

@Service
public class WendaService {
    public String getMassage(int userId){
        return "Hello Message:"+ String.valueOf(userId);
    }
}
