package com.service;

import com.model.BenhNhan;
import com.model.DonViDieuTri;
import com.model.TinhThanh;
import com.repository.BenhNhanRepository;
import com.repository.DonViDieuTriRepository;
import com.repository.TinhThanhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing BenhNhan (patients).
 */
@Service
public class BenhNhanService {

    private final DonViDieuTriService donViDieuTriService;
    private TinhThanhRepository tinhThanhRepository;
    private DonViDieuTriRepository donViDieuTriRepository;
    private BenhNhanRepository benhNhanRepository;

    /**
     * Constructor for BenhNhanService.
     *
     * @param benhNhanRepository     Repository for BenhNhan
     * @param donViDieuTriRepository Repository for DonViDieuTri
     * @param tinhThanhRepository    Repository for TinhThanh
     * @param donViDieuTriService    Service for DonViDieuTri
     */
    @Autowired
    public BenhNhanService(BenhNhanRepository benhNhanRepository, DonViDieuTriRepository donViDieuTriRepository,
                           TinhThanhRepository tinhThanhRepository, DonViDieuTriService donViDieuTriService) {
        this.benhNhanRepository = benhNhanRepository;
        this.donViDieuTriRepository = donViDieuTriRepository;
        this.tinhThanhRepository = tinhThanhRepository;
        this.donViDieuTriService = donViDieuTriService;
    }

    /**
     * Creates a new BenhNhan (patient).
     *
     * @param benhNhan The patient to be created.
     * @return The saved patient entity.
     * @throws RuntimeException If the associated DonViDieuTri or TinhThanh is not found.
     */
    public BenhNhan createBenhNhan(BenhNhan benhNhan) {
        // Kiểm tra trùng mã bệnh nhân
        if (benhNhanRepository.existsById(benhNhan.getMaBenhNhan())) {
            throw new RuntimeException("Mã bệnh nhân đã tồn tại trong hệ thống");
        }
        DonViDieuTri donViDieuTri = donViDieuTriRepository.findById(benhNhan.getDonViDieuTri().getMaDonVi())
                .orElseThrow(() -> new RuntimeException("Mã đơn vị không tồn tại trong hệ thống"));
        TinhThanh tinhThanh = tinhThanhRepository.findById(benhNhan.getTinhThanh().getMaTinhThanh())
                .orElseThrow(() -> new RuntimeException("Mã tỉnh thành không tồn tại trong hệ thống"));
        // Kiểm tra xem đơn vị điều trị có thuộc về tỉnh thành của bệnh nhân không
        String maTinhCuaDonVi = donViDieuTriRepository.getMaTinhByMaDonVi(donViDieuTri.getMaDonVi());
        if (!maTinhCuaDonVi.equals(tinhThanh.getMaTinhThanh())) {
            throw new RuntimeException("Đơn vị điều trị không thuộc về tỉnh thành mà bệnh nhân đang sinh sống");
        }
        // Gán lại các đối tượng đã tìm thấy
        benhNhan.setDonViDieuTri(donViDieuTri);
        benhNhan.setTinhThanh(tinhThanh);
        return benhNhanRepository.save(benhNhan);
    }

    /**
     * Retrieves all patients.
     *
     * @return A list of all patients.
     */
    public List<BenhNhan> getAllBenhNhan() {
        return benhNhanRepository.findAll();
    }

    /**
     * Retrieves a list of patients by their identification number (CMND).
     *
     * @param soCMND The identification number.
     * @return A list of matching patients.
     */
    public List<BenhNhan> getBenhNhansBySoCMND(String soCMND) {
        System.out.println(soCMND);
        return benhNhanRepository.getBenhNhansBySoCMND(soCMND);
    }

    /**
     * Retrieves a single patient by their identification number (CMND).
     *
     * @param soCMND The identification number.
     * @return The matching patient.
     */
    public BenhNhan getBenhNhansBySoCMND1(String soCMND) {
        System.out.println(soCMND);
        return benhNhanRepository.getBenhNhansBySoCMND1(soCMND);
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param maBenhNhan The patient ID.
     * @return The found patient.
     * @throws RuntimeException If no patient is found with the given ID.
     */
    public Optional<BenhNhan> getBenhNhanById(String maBenhNhan) {
        Optional<BenhNhan> optionalBenhNhan = benhNhanRepository.findById(maBenhNhan);
        return optionalBenhNhan;
    }

    /**
     * Updates an existing patient record.
     *
     * @param id The ID of the patient to update.
     * @param benhNhan The updated patient data.
     * @return The updated patient entity.
     * @throws RuntimeException If the patient is not found or the assigned treatment unit (DonViDieuTri) does not belong to the patient's province (TinhThanh).
     */
    public BenhNhan updateBenhNhan(String id, BenhNhan benhNhan) {
        BenhNhan existing = benhNhanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với ID: " + id));
        String maDonViCuaBenhNhan = benhNhan.getDonViDieuTri().getMaDonVi();
        String maTinhCuaDonVi = donViDieuTriRepository.getMaTinhByMaDonVi(maDonViCuaBenhNhan);

        if (maTinhCuaDonVi.equals(existing.getTinhThanh().getMaTinhThanh())) {
            existing.setNgayCachLy(benhNhan.getNgayCachLy());
            existing.setDonViDieuTri(benhNhan.getDonViDieuTri());
            return benhNhanRepository.save(existing);
        } else {
            throw new RuntimeException("Don vi dieu tri khong thuoc ve tinh thanh ma benh nhan dang sinh song");
        }
    }

    /**
     * Xóa bệnh nhân theo mã
     * 
     * @param maBenhNhan Mã bệnh nhân cần xóa
     * @throws RuntimeException Nếu không tìm thấy bệnh nhân hoặc có lỗi khi xóa
     */
    public void deleteBenhNhan(String maBenhNhan) {
        // Kiểm tra bệnh nhân có tồn tại không
        if (!benhNhanRepository.existsById(maBenhNhan)) {
            throw new RuntimeException("Không tìm thấy bệnh nhân với mã: " + maBenhNhan);
        }
        
        // Thực hiện xóa
        benhNhanRepository.deleteById(maBenhNhan);
    }

    public List<BenhNhan> getAllBenhNhanSorted(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return benhNhanRepository.findAll(sort);
    }
}