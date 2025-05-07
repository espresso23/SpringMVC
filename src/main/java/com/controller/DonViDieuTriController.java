package com.controller;

import com.model.DonViDieuTri;
import com.model.TinhThanh;
import com.service.DonViDieuTriService;
import com.service.TinhThanhService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.persistence.*;

import java.util.List;

@Controller
@RequestMapping("/don-vi-dieu-tri")
public class DonViDieuTriController {
    private final DonViDieuTriService donViDieuTriService;
    private final TinhThanhService tinhThanhService;

    @Autowired
    public DonViDieuTriController(DonViDieuTriService donViDieuTriService,
                                 TinhThanhService tinhThanhService) {
        this.donViDieuTriService = donViDieuTriService;
        this.tinhThanhService = tinhThanhService;
    }

    @GetMapping("/danh-sach")
    public String listDonViDieuTri(Model model) {
        model.addAttribute("donViDieuTriList", donViDieuTriService.getAllDonViDieuTri());
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        return "don-vi-dieu-tri/danh-sach";
    }

    @GetMapping("/them-moi")
    public String showAddForm(Model model) {

        model.addAttribute("donViDieuTri", new DonViDieuTri());
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        return "don-vi-dieu-tri/them-moi";
    }

    @PostMapping("/them-moi")
    public String addDonViDieuTri(@ModelAttribute("donViDieuTri") DonViDieuTri donViDieuTri,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "don-vi-dieu-tri/them-moi";
        }
        DonViDieuTri existDonViDieuTri = donViDieuTriService.getDonViDieuTriById(donViDieuTri.getMaDonVi());
        if (existDonViDieuTri != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn vị điều trị đã tồn tại!");
            return "redirect:/don-vi-dieu-tri/them-moi";
        }

        try {
            // Lấy tỉnh thành theo mã và thiết lập cho đơn vị điều trị
            if (donViDieuTri.getTinhThanh() != null && donViDieuTri.getTinhThanh().getMaTinhThanh() != null) {
                String maTinhThanh = donViDieuTri.getTinhThanh().getMaTinhThanh();
                TinhThanh tinhThanh = tinhThanhService.getTinhThanhById(maTinhThanh);
                
                if (tinhThanh == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy tỉnh thành với mã: " + maTinhThanh);
                    return "redirect:/don-vi-dieu-tri/them-moi";
                }
                
                donViDieuTri.setTinhThanh(tinhThanh);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn tỉnh thành");
                return "redirect:/don-vi-dieu-tri/them-moi";
            }
            
            donViDieuTriService.addDonViDieuTri(donViDieuTri);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn vị điều trị thành công!");
            return "redirect:/don-vi-dieu-tri/danh-sach";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/don-vi-dieu-tri/them-moi";
        }
    }

    @GetMapping("/chinh-sua/{maDonVi}")
    public String showEditForm(@PathVariable("maDonVi") String maDonVi, Model model) {
        System.out.println("==> [DonViDieuTriController] GET /chinh-sua/" + maDonVi);
        DonViDieuTri donViDieuTri = donViDieuTriService.getDonViDieuTriById(maDonVi);
        if (donViDieuTri == null) {
            System.out.println("==> [DonViDieuTriController] Không tìm thấy đơn vị điều trị: " + maDonVi);
            return "redirect:/don-vi-dieu-tri/danh-sach";
        }
        model.addAttribute("donViDieuTri", donViDieuTri);
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        return "don-vi-dieu-tri/chinh-sua";
    }
    @PostMapping("/chinh-sua/{maDonVi}")
    public String updateDonViDieuTri(@PathVariable("maDonVi") String maDonVi,
                                    @Valid @ModelAttribute("donViDieuTri") DonViDieuTri donViDieuTri,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        // Kiểm tra đơn vị điều trị có tồn tại không
        DonViDieuTri existingDonVi = donViDieuTriService.getDonViDieuTriById(maDonVi);
        if (existingDonVi == null) {
            return "redirect:/don-vi-dieu-tri/danh-sach";
        }

        if (result.hasErrors()) {
            model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
            return "don-vi-dieu-tri/chinh-sua";
        }

        try {
            // Xử lý tỉnh thành
            if (donViDieuTri.getTinhThanh() != null && donViDieuTri.getTinhThanh().getMaTinhThanh() != null) {
                String maTinhThanh = donViDieuTri.getTinhThanh().getMaTinhThanh();
                TinhThanh tinhThanh = tinhThanhService.getTinhThanhById(maTinhThanh);
                
                if (tinhThanh == null) {
                    model.addAttribute("errorMessage", "Không tìm thấy tỉnh thành với mã: " + maTinhThanh);
                    model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
                    return "don-vi-dieu-tri/chinh-sua";
                }
                
                donViDieuTri.setTinhThanh(tinhThanh);
            } else {
                model.addAttribute("errorMessage", "Vui lòng chọn tỉnh thành");
                model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
                return "don-vi-dieu-tri/chinh-sua";
            }
            
            donViDieuTriService.updateDonViDieuTri(maDonVi, donViDieuTri);
            redirectAttributes.addFlashAttribute("success", "Cập nhật đơn vị điều trị thành công!");
            return "redirect:/don-vi-dieu-tri/danh-sach";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
            return "don-vi-dieu-tri/chinh-sua";
        }
    }

    @GetMapping("/xoa/{maDonVi}")
    public String deleteDonViDieuTri(@PathVariable("maDonVi") String maDonVi,
                                    RedirectAttributes redirectAttributes) {
        try {
            donViDieuTriService.deleteDonViDieuTri(maDonVi);
            redirectAttributes.addFlashAttribute("success", "Xóa đơn vị điều trị thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/don-vi-dieu-tri/danh-sach";
    }
    
    /**
     * API endpoint để lấy danh sách đơn vị điều trị theo mã tỉnh thành
     * @param maTinhThanh Mã tỉnh thành cần lọc
     * @return Danh sách đơn vị điều trị thuộc tỉnh thành
     */
    @GetMapping("/api/by-tinh-thanh")
    @ResponseBody
    public List<DonViDieuTri> getDonViDieuTriByTinhThanh(@RequestParam("maTinhThanh") String maTinhThanh) {
        return donViDieuTriService.getDonViDieuTriByTinhThanh(maTinhThanh);
    }
} 