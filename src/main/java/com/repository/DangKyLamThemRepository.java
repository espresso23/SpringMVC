package com.repository;

import com.model.DangKyLamThem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DangKyLamThemRepository extends JpaRepository<DangKyLamThem, String> {
    // Tìm theo mã việc làm và trạng thái không phải Closed
    @Query("SELECT d FROM DangKyLamThem d WHERE d.viecLam.maVL LIKE CONCAT('%', :maVL, '%') AND d.viecLam.trangThai <> 'Closed'")
    List<DangKyLamThem> findByViecLam_MaVLContainingAndViecLam_TrangThaiNot(@Param("maVL") String maVL);

    // Tìm theo tên nhân viên và trạng thái không phải Closed
    @Query("SELECT d FROM DangKyLamThem d WHERE d.hoTen LIKE CONCAT('%', :hoTen, '%') AND d.viecLam.trangThai <> 'Closed'")
    List<DangKyLamThem> findByHoTenContainingAndViecLam_TrangThaiNotClosed(@Param("hoTen") String hoTen);

    @Query("SELECT d FROM DangKyLamThem d WHERE d.viecLam.trangThai <> 'Closed'")
    List<DangKyLamThem> getDangKyNotClosed();
}
