package com.controller;

// Import required classes and annotations

import com.model.BenhNhan;
import com.model.DonViDieuTri;
import com.model.TinhThanh;
import com.service.BenhNhanService;
import com.service.DonViDieuTriService;
import com.service.TinhThanhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/benhnhan") // Base URL for handling requests related to BenhNhan
public class BenhNhanController {
    private BenhNhanService benhNhanService;
    private TinhThanhService tinhThanhService;
    private DonViDieuTriService donViDieuTriService;

    // Constructor-based dependency injection
    @Autowired
    public BenhNhanController(BenhNhanService benhNhanService, 
                             TinhThanhService tinhThanhService,
                             DonViDieuTriService donViDieuTriService) {
        this.benhNhanService = benhNhanService;
        this.tinhThanhService = tinhThanhService;
        this.donViDieuTriService = donViDieuTriService;
    }

    // Display the form to add a new BenhNhan
    @GetMapping("")
    public String showPageBenhNhan(Model model) {
        model.addAttribute("benhNhan", new BenhNhan());// Add an empty BenhNhan object to the model
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        model.addAttribute("donViList", donViDieuTriService.getAllDonViDieuTri());
        return "addBenhNhan"; // Return the view for adding BenhNhan
    }

    // Handle form submission for adding a new BenhNhan
    @PostMapping("/addBenhNhan")
    public String addBenhNhan(@ModelAttribute("benhNhan") BenhNhan benhNhan, RedirectAttributes redirectAttributes) {
        try {
            System.out.println(benhNhan.toString());
            Optional<BenhNhan> existingBenhNhan = benhNhanService.getBenhNhanById(benhNhan.getMaBenhNhan());
            if (existingBenhNhan.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mã bệnh nhân '" + benhNhan.getMaBenhNhan() + "' đã tồn tại!");
                return "redirect:/benhnhan";
            }
            // Gọi service để tạo mới
            benhNhanService.createBenhNhan(benhNhan);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm bệnh nhân thành công!");
            return "redirect:/benhnhan/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi thêm bệnh nhân: " + e.getMessage());
            return "redirect:/benhnhan";
        }
    }

    // Display the list of BenhNhan
    @GetMapping("/list")
    public String showListBenhNhan(
            @RequestParam(value = "sort", defaultValue = "tenBenhNhan") String sortBy,
            @RequestParam(value = "dir", defaultValue = "asc") String direction,
            Model model) {
        List<BenhNhan> listBenhNhan = benhNhanService.getAllBenhNhanSorted(sortBy, direction);
        model.addAttribute("listBenhNhan", listBenhNhan);
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        model.addAttribute("donViList", donViDieuTriService.getAllDonViDieuTri());
        return "listBenhNhan";
    }

    // Display search results (optional)
    @GetMapping("/listsearch")
    public String showListBenhNhansearh(Model model) {
        // Không cần dùng redirect attributes vì đang truy cập trực tiếp đến view
        List<BenhNhan> listBenhNhan = benhNhanService.getAllBenhNhan(); // Fetch all BenhNhan
        model.addAttribute("listBenhNhan", listBenhNhan); // Sử dụng model trực tiếp thay vì redirectAttributes
        // Thêm danh sách tỉnh thành và đơn vị điều trị cho bộ lọc
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        model.addAttribute("donViList", donViDieuTriService.getAllDonViDieuTri());
        System.out.println("Debug - Số lượng bệnh nhân (search): " + (listBenhNhan != null ? listBenhNhan.size() : "null"));
        return "listBenhNhan"; // Return the view for displaying the list
    }

    // Search BenhNhan by "soCMND"
    @GetMapping("/search")
    public String searchByCMND(@RequestParam(value = "soCMND", required = false) String soCMND,
                               Model model) {
        
        // Mặc định lấy tất cả bệnh nhân
        List<BenhNhan> benhNhanList = new ArrayList<>();
        String message = null;
        
        try {
            // Nếu có tìm kiếm theo CMND
            if (soCMND != null && !soCMND.trim().isEmpty()) {
                benhNhanList = benhNhanService.getBenhNhansBySoCMND(soCMND.trim());
                
                if (benhNhanList.isEmpty()) {
                    message = "Không tìm thấy bệnh nhân với số CMND: '" + soCMND + "'";
                } else {
                    model.addAttribute("searchKeyword", soCMND);
                }

            } else {
                // Nếu không có tiêu chí tìm kiếm nào, lấy tất cả bệnh nhân
                benhNhanList = benhNhanService.getAllBenhNhan();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm kiếm bệnh nhân: " + e.getMessage());
            e.printStackTrace();
            message = "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage();
            // Đảm bảo danh sách không null
            benhNhanList = new ArrayList<>();
        }
        
        // In ra log để debug
        System.out.println("Debug - Tìm kiếm - Số lượng bệnh nhân: " + (benhNhanList != null ? benhNhanList.size() : "danh sách null"));
        
        // Đảm bảo danh sách không null
        if (benhNhanList == null) {
            benhNhanList = new ArrayList<>();
        }
        
        model.addAttribute("listBenhNhan", benhNhanList);
        
        if (message != null) {
            model.addAttribute("notFoundMessage", message);
        }
        
        return "listBenhNhan";
    }

    //show form edit benh nhan
    @GetMapping("/edit/{maBenhNhan}")
    public String showFormEdit(@PathVariable("maBenhNhan") String maBenhNhan, Model model, RedirectAttributes redirectAttributes) {
        Optional<BenhNhan> benhNhanOpt = benhNhanService.getBenhNhanById(maBenhNhan);

        if (benhNhanOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bệnh nhân không tồn tại.");
            return "redirect:/benhnhan/list";
        }

        BenhNhan benhNhan = benhNhanOpt.get();//get ra doi tuong benh nhan
        model.addAttribute("benhNhan", benhNhan);
        model.addAttribute("donViList", donViDieuTriService.getAllDonViDieuTri());
        return "editBenhNhan";
    }

    // Handle form submission for updating a BenhNhan
    @PostMapping("/update/{maBenhNhan}")
    public String editBenhNhan(@PathVariable("maBenhNhan") String maBenhNhan,
                               @ModelAttribute("benhNhan") BenhNhan benhNhan,
                               RedirectAttributes redirectAttributes) {
        try {
            benhNhanService.updateBenhNhan(maBenhNhan, benhNhan);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
            return "redirect:/benhnhan/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
            return "redirect:/benhnhan/edit/" + maBenhNhan;
        }
    }

    /**
     * Xóa bệnh nhân theo mã
     */
    @GetMapping("/delete/{maBenhNhan}")
    public String deleteBenhNhan(@PathVariable("maBenhNhan") String maBenhNhan,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra bệnh nhân có tồn tại không
            Optional<BenhNhan> benhNhan = benhNhanService.getBenhNhanById(maBenhNhan);
            if (benhNhan == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bệnh nhân với mã: " + maBenhNhan);
                return "redirect:/benhnhan/list";
            }
            
            // Thực hiện xóa
            benhNhanService.deleteBenhNhan(maBenhNhan);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa bệnh nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa bệnh nhân: " + e.getMessage());
        }
        return "redirect:/benhnhan/list";
    }

}