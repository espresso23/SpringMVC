package com.repository;

import com.model.DonViDieuTri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonViDieuTriRepository extends JpaRepository<DonViDieuTri, String> {
    @Query("SELECT d.tinhThanh.maTinhThanh FROM DonViDieuTri d where d.maDonVi = :maDonVi")
    String getMaTinhByMaDonVi(@Param("maDonVi") String maDonVi);
}
