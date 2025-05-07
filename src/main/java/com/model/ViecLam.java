package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "VIECLAM")
public class ViecLam {

    @Id
    @Column(name = "MaVL")
    private String maVL;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "DuAn")
    private String duAn;

    @Column(name = "NgonNgu")
    private String ngonNgu;

    @Column(name = "NgayBD")
    private LocalDate ngayBD;

    @Column(name = "NgayKT")
    private LocalDate  ngayKT;

    @Column(name = "TongNgayCong")
    private int tongNgayCong;

    @Column(name = "TrangThai")
    private String trangThai;

    // Getters and Setters
    public String getMaVL() {
        return maVL;
    }

    public void setMaVL(String maVL) {
        this.maVL = maVL;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDuAn() {
        return duAn;
    }

    public void setDuAn(String duAn) {
        this.duAn = duAn;
    }

    public String getNgonNgu() {
        return ngonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        this.ngonNgu = ngonNgu;
    }

    public LocalDate  getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(LocalDate  ngayBD) {
        this.ngayBD = ngayBD;
    }

    public LocalDate  getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(LocalDate  ngayKT) {
        this.ngayKT = ngayKT;
    }

    public int getTongNgayCong() {
        return tongNgayCong;
    }

    public void setTongNgayCong(int tongNgayCong) {
        this.tongNgayCong = tongNgayCong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}