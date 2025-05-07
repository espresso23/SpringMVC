package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "DANGKYLAMTHEM")
public class DangKyLamThem {

    @Id
    @Column(name = "MaDK")
    private String maDK;

    @Column(name = "MaNV")
    @NotBlank(message = "Ma NV khong duoc de trong")
    private String maNV;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "GioiTinh")
    private String gioiTinh;

    @Column(name = "CapBac")
    private int capBac;

    @ManyToOne
    @JoinColumn(name = "MaVL", nullable = false)
    private ViecLam viecLam;

    @Column(name = "SoNgayCong")
    @Min(value = 1, message = "Số ngày công tối thiểu là 1")
    @Max(value = 31, message = "Số ngày công tối đa là 31")
    private int soNgayCong;

    @Column(name = "TongTien")
    private double tongTien;

    // Getters and Setters
    public String getMaDK() {
        return maDK;
    }

    public void setMaDK(String maDK) {
        this.maDK = maDK;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getCapBac() {
        return capBac;
    }

    public void setCapBac(int capBac) {
        this.capBac = capBac;
    }

    public ViecLam getViecLam() {
        return viecLam;
    }

    public void setViecLam(ViecLam viecLam) {
        this.viecLam = viecLam;
    }

    public int getSoNgayCong() {
        return soNgayCong;
    }

    public void setSoNgayCong(int soNgayCong) {
        this.soNgayCong = soNgayCong;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}