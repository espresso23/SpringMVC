package com.service;

import com.model.TinhThanh;
import com.repository.TinhThanhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TinhThanhService {
    private TinhThanhRepository tinhThanhRepository;

    @Autowired
    public TinhThanhService(TinhThanhRepository tinhThanhRepository) {
        this.tinhThanhRepository = tinhThanhRepository;
    }

    public List<TinhThanh> getAllTinhThanh() {
        return tinhThanhRepository.findAll();
    }

    public TinhThanh getTinhThanhById(String maTinhThanh) {
        return tinhThanhRepository.findById(maTinhThanh).orElse(null);
    }

    public TinhThanh addTinhThanh(TinhThanh tinhThanh) {
        return tinhThanhRepository.save(tinhThanh);
    }

    public void deleteTinhThanh(String maTinhThanh) {
        tinhThanhRepository.deleteById(maTinhThanh);
    }

    public void updateTinhThanh(String maTinhThanh, TinhThanh tinhThanh) {
        TinhThanh existingTinhThanh = getTinhThanhById(maTinhThanh);
        if (existingTinhThanh == null) {
            throw new IllegalArgumentException("Không tìm thấy tỉnh thành với mã: " + maTinhThanh);
        }
        tinhThanh.setMaTinhThanh(maTinhThanh);
        tinhThanhRepository.save(tinhThanh);
    }
}
