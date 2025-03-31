package com.service;

import com.repository.DonViDieuTriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
