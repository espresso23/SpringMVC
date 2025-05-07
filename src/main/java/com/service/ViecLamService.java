package com.service;

import com.model.ViecLam;
import com.repository.ViecLamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViecLamService {
    @Autowired
    private ViecLamRepository viecLamRepository;

    public List<ViecLam> getAllViecLam() {
        return viecLamRepository.findAll();
    }
    public ViecLam saveViecLam(ViecLam viecLam) {
        // Validate dữ liệu nếu cần (ví dụ: mã việc làm không trùng)
        if (viecLamRepository.existsById(viecLam.getMaVL())) {
            throw new RuntimeException("Ma viec lam da ton tai");
        }
        return viecLamRepository.save(viecLam);
    }

    public ViecLam getViecLamById(String maVL) {
        return viecLamRepository.findById(maVL).orElse(null);
    }

    public ViecLam updateViecLam(String maVL, ViecLam viecLam) {
        ViecLam existing = viecLamRepository.findById(maVL)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy việc làm với mã: " + maVL));
        // Cập nhật các trường
        existing.setMoTa(viecLam.getMoTa());
        existing.setDuAn(viecLam.getDuAn());
        existing.setNgonNgu(viecLam.getNgonNgu());
        existing.setNgayBD(viecLam.getNgayBD());
        existing.setNgayKT(viecLam.getNgayKT());
        existing.setTongNgayCong(viecLam.getTongNgayCong());
        existing.setTrangThai(viecLam.getTrangThai());
        return viecLamRepository.save(existing);
    }
}
