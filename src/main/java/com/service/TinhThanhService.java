package com.service;

import com.repository.TinhThanhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TinhThanhService {
    private TinhThanhRepository tinhThanhRepository;

    @Autowired
    public TinhThanhService(TinhThanhRepository tinhThanhRepository) {
        this.tinhThanhRepository = tinhThanhRepository;
    }
}
