package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class BenhNhan {
    @Id
    private String maBenNhan;

    private String tenBenhNhan;

    private String soCMND;

    private String gioiTinh;

    private LocalDate ngaySinh;

    private String diaChi;

    private String soDienThoai;

    @ManyToOne
    @JoinColumn(name = "maTinhThanh")
    private TinhThanh tinhThanh;

    private LocalDate ngayCachLy;

    @ManyToOne
    @JoinColumn(name = "maDonVi")
    private DonViDieuTri donViDieuTri;

    public DonViDieuTri getDonViDieuTri() {
        return donViDieuTri;
    }

    public void setDonViDieuTri(DonViDieuTri donViDieuTri) {
        this.donViDieuTri = donViDieuTri;
    }

    public TinhThanh getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(TinhThanh tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMaBenNhan() {
        return maBenNhan;
    }

    public void setMaBenNhan(String maBenNhan) {
        this.maBenNhan = maBenNhan;
    }

    public LocalDate getNgayCachLy() {
        return ngayCachLy;
    }

    public void setNgayCachLy(LocalDate ngayCachLy) {
        this.ngayCachLy = ngayCachLy;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenBenhNhan() {
        return tenBenhNhan;
    }

    public void setTenBenhNhan(String tenBenhNhan) {
        this.tenBenhNhan = tenBenhNhan;
    }
}
