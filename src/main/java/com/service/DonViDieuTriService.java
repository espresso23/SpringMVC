package com.service;

import com.model.DonViDieuTri;
import com.repository.DonViDieuTriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonViDieuTriService {
    private DonViDieuTriRepository donViDieuTriRepository;

    @Autowired
    public DonViDieuTriService(DonViDieuTriRepository donViDieuTriRepository) {
        this.donViDieuTriRepository = donViDieuTriRepository;
    }

    public String getMaTinhByDonVi(String maDonVi) {
        return donViDieuTriRepository.getMaTinhByMaDonVi(maDonVi);
    }
    public List<DonViDieuTri> getAllDonViDieuTri() {
        return donViDieuTriRepository.findAll();
    }
    public void addDonViDieuTri(DonViDieuTri donViDieuTri) {
        donViDieuTriRepository.save(donViDieuTri);
    }
    public void deleteDonViDieuTri(String maDonVi) {
        donViDieuTriRepository.deleteById(maDonVi);
    }
    public DonViDieuTri getDonViDieuTriById(String maDonVi) {
        return donViDieuTriRepository.findById(maDonVi).orElse(null);
    }
    public void updateDonViDieuTri(String maDonVi, DonViDieuTri donViDieuTri) {
        DonViDieuTri donViDieuTriOld = donViDieuTriRepository.findById(maDonVi).orElse(null);
        if (donViDieuTriOld != null) {
            donViDieuTri.setMaDonVi(donViDieuTriOld.getMaDonVi());
            donViDieuTriRepository.save(donViDieuTri);
        }
    }
    
    /**
     * Lấy danh sách đơn vị điều trị theo mã tỉnh thành
     * @param maTinhThanh Mã tỉnh thành cần lọc
     * @return Danh sách đơn vị điều trị thuộc tỉnh thành
     */
    public List<DonViDieuTri> getDonViDieuTriByTinhThanh(String maTinhThanh) {
        return donViDieuTriRepository.findByTinhThanhMaTinhThanh(maTinhThanh);
    }
}
