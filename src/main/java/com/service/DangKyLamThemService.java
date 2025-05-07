package com.service;


import com.model.DangKyLamThem;
import com.model.ViecLam;
import com.repository.DangKyLamThemRepository;
import com.repository.ViecLamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DangKyLamThemService {
    @Autowired
    private DangKyLamThemRepository dangKyLamThemRepository;

    @Autowired
    private ViecLamRepository viecLamRepository;

    public List<DangKyLamThem> getAllDangKyLamThem() {
        return dangKyLamThemRepository.findAll();
    }

    public DangKyLamThem saveDangKyLamThem(DangKyLamThem dangKyLamThem) {
        // Kiểm tra mã việc làm có tồn tại không
        if (dangKyLamThemRepository.existsById(dangKyLamThem.getMaDK())) {
            throw new RuntimeException("Ma DK lam da ton tai");//1 cau
        }
        ViecLam viecLam = viecLamRepository.findById(dangKyLamThem.getViecLam().getMaVL())
                .orElseThrow(() -> new RuntimeException("Khong ton tai viec lam trong he thong"));//2 cau

        // Kiểm tra trạng thái việc làm
        if (viecLam.getTrangThai().equals("Closed")) {
            throw new RuntimeException("Viec lam dang chon da duoc dong");//3 cau
        }

        // Validate số ngày công (phía server, nếu cần)
        if (dangKyLamThem.getSoNgayCong() <= 0 || dangKyLamThem.getSoNgayCong() > 20) {
            throw new RuntimeException("So ngay cong phai la so lon hon 0 va nho hon hoac bang 20");//4 cau
        }

        // Tính toán tổng tiền
        double donGia = 0;
        switch (dangKyLamThem.getCapBac()) {
            case 1:
                donGia = 500000 * dangKyLamThem.getSoNgayCong();
                break;
            case 2:
                donGia = 600000 * dangKyLamThem.getSoNgayCong();
                break;
            case 3:
                donGia = 700000 * dangKyLamThem.getSoNgayCong();
                break;
            default:
                throw new RuntimeException("Cap bac khong hop le");
        }

        double tongTien = dangKyLamThem.getSoNgayCong() * donGia;
        if (viecLam.getNgonNgu().equalsIgnoreCase("COBOL") || viecLam.getNgonNgu().equalsIgnoreCase("RPG")) {
            tongTien *= 1.1; // Thêm 10% nếu là ngôn ngữ đặc thù
        }

        dangKyLamThem.setTongTien(tongTien);

        // Lưu vào database
        return dangKyLamThemRepository.save(dangKyLamThem);
    }

    public DangKyLamThem updateDangKyLamThem(String maDK, DangKyLamThem dangKyLamThem) {
        DangKyLamThem existingDangKy = dangKyLamThemRepository.findById(maDK)
                .orElseThrow(() -> new RuntimeException("Khong ton tai dang ky viec lam"));

        // Validate số ngày công
        if (dangKyLamThem.getSoNgayCong() > existingDangKy.getSoNgayCong()) {
            throw new RuntimeException("Error_20040223: So ngay cong thay doi khong hop le");//5 cau
        }

        // Tính toán lại tổng tiền nếu cần
        if (!dangKyLamThem.getViecLam().getMaVL().equals(existingDangKy.getViecLam().getMaVL()) ||
                dangKyLamThem.getSoNgayCong() != existingDangKy.getSoNgayCong() ||
                dangKyLamThem.getCapBac() != existingDangKy.getCapBac()) {

            ViecLam viecLam = viecLamRepository.findById(dangKyLamThem.getViecLam().getMaVL())
                    .orElseThrow(() -> new RuntimeException("Khong ton tai viec lam trong he thong"));

            double donGia = switch (dangKyLamThem.getCapBac()) {
                case 1 -> 500000;
                case 2 -> 600000;
                case 3 -> 700000;
                default -> throw new RuntimeException("Cap bac khong hop le");
            };

            double tongTien = dangKyLamThem.getSoNgayCong() * donGia;
            if (viecLam.getNgonNgu().equals("COBOL") || viecLam.getNgonNgu().equals("RPG")) {
                tongTien *= 1.1;
            }
            existingDangKy.setTongTien(tongTien);
        }

        // Cập nhật TẤT CẢ các trường
        existingDangKy.setMaNV(dangKyLamThem.getMaNV());
        existingDangKy.setHoTen(dangKyLamThem.getHoTen());
        existingDangKy.setGioiTinh(dangKyLamThem.getGioiTinh());
        existingDangKy.setCapBac(dangKyLamThem.getCapBac());
        existingDangKy.setViecLam(dangKyLamThem.getViecLam());
        existingDangKy.setSoNgayCong(dangKyLamThem.getSoNgayCong());

        return dangKyLamThemRepository.save(existingDangKy);
    }

    public DangKyLamThem getDangKyLamThemById(String maDK) {
        return dangKyLamThemRepository.findById(maDK).orElse(null);
    }

    public void deleteDangKyLamThem(String maDK) {
        dangKyLamThemRepository.deleteById(maDK);
    }
    // Lấy danh sách không bao gồm trạng thái "Closed"
    public List<DangKyLamThem> getAllDangKyLamThemNotClosed() {
        return dangKyLamThemRepository.findAll().stream()
                .filter(dk -> !dk.getViecLam().getTrangThai().equals("Closed"))
                .collect(Collectors.toList());
    }

    // Tìm kiếm đa năng
    public List<DangKyLamThem> searchDangKyLamThem(String keyword, String searchType) {
        if (keyword == null || keyword.isEmpty()) {
            return dangKyLamThemRepository.getDangKyNotClosed();
        }

        return switch (searchType) {
            case "maVL" -> dangKyLamThemRepository.findByViecLam_MaVLContainingAndViecLam_TrangThaiNot(keyword);
            case "hoTen" -> dangKyLamThemRepository.findByHoTenContainingAndViecLam_TrangThaiNotClosed(keyword);
            default -> dangKyLamThemRepository.getDangKyNotClosed();
        };
    }
    public boolean findByID(String id) {
        return dangKyLamThemRepository.findById(id).isPresent();
    }
}
