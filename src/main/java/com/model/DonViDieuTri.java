package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DonViDieuTri {
    @Id
    private String maDonVi;

    private String tenDonVi;

    private String diaChi;

    @ManyToOne
    @JoinColumn(name = "maTinhThanh")
    private TinhThanh tinhThanh;

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaDonVi() {
        return maDonVi;
    }

    public void setMaDonVi(String maDonVi) {
        this.maDonVi = maDonVi;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
        this.tenDonVi = tenDonVi;
    }

    public TinhThanh getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(TinhThanh tinhThanh) {
        this.tinhThanh = tinhThanh;
    }
}
