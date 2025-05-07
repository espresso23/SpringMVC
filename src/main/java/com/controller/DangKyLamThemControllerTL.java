package com.controller;


import com.model.DangKyLamThem;
import com.service.DangKyLamThemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dangky")
public class DangKyLamThemControllerTL {
    @Autowired
    private DangKyLamThemService dangKyLamThemService;

    // Hiển thị danh sách đăng ký làm thêm
    @GetMapping
    public String getAllDangKyLamThem(Model model) {
        try {
            // Kiểm tra xem đã có danh sách từ search chưa
            if (!model.containsAttribute("dangKyLamThemList")) {
                List<DangKyLamThem> dangKyLamThemList = dangKyLamThemService.getAllDangKyLamThemNotClosed();
                model.addAttribute("dangKyLamThemList", dangKyLamThemList);
            }
            model.addAttribute("dangKyLamThem", new DangKyLamThem());
            return "addDangKyLamThem";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Xử lý request POST để lưu thông tin đăng ký làm thêm
     * @param dangKyLamThem Đối tượng chứa dữ liệu từ form đăng ký, được validate tự động
     * @param result Đối tượng chứa kết quả validate và binding dữ liệu
     * @param redirectAttributes Đối tượng để truyền dữ liệu qua redirect
     * @return Chuỗi redirect đến trang đăng ký
     */
    @PostMapping("/save")
    public String saveDangKyLamThem(
            // Đối tượng DangKyLamThem được tự động bind từ form data và validate
            @Valid @ModelAttribute DangKyLamThem dangKyLamThem,
            // Kết quả validate và binding sẽ được lưu trong result
            BindingResult result,
            // Dùng để truyền thông điệp/flash attributes qua redirect
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu có lỗi validate
        if (result.hasErrors()) {
            // Lưu thông tin lỗi vào flash attribute để giữ lại qua redirect
            // Spring yêu cầu đặt tên theo quy tắc "org.springframework.validation.BindingResult.[tên model]"
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.dangKyLamThem",
                    result);

            // Lưu lại dữ liệu đã nhập để hiển thị lại trên form
            redirectAttributes.addFlashAttribute("dangKyLamThem", dangKyLamThem);

            // Redirect về trang đăng ký để hiển thị lỗi
            return "redirect:/dangky";
        }

        try {
            // Gọi service để lưu dữ liệu vào database
            dangKyLamThemService.saveDangKyLamThem(dangKyLamThem);

            // Thêm thông báo thành công sẽ hiển thị trên trang đích
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Đăng ký thành công!");

        } catch (Exception e) {
            // Nếu có lỗi khi lưu, thêm thông báo lỗi
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Lỗi hệ thống: " + e.getMessage());

            // Đồng thời throw exception để xử lý ở lớp cao hơn (GlobalExceptionHandler)
            throw new RuntimeException("Lỗi khi lưu đăng ký làm thêm: " + e.getMessage(), e);
        }

        // Sau khi xử lý xong, redirect về trang đăng ký
        // Các flash attributes đã thêm sẽ tự động được truyền qua
        return "redirect:/dangky";
    }
    @GetMapping("/search")
    public String search(
            @RequestParam("keyword") String keyword,
            @RequestParam("searchType") String searchType,
            RedirectAttributes redirectAttributes) {

        System.out.println("Search keyword: " + keyword);
        System.out.println("Search type: " + searchType);

        List<DangKyLamThem> dangKyLamThemList = dangKyLamThemService.searchDangKyLamThem(keyword, searchType);
        redirectAttributes.addFlashAttribute("dangKyLamThemList", dangKyLamThemList);
        // Thêm keyword và searchType để hiển thị lại trên form
        redirectAttributes.addFlashAttribute("keyword", keyword);
        redirectAttributes.addFlashAttribute("searchType", searchType);
        return "redirect:/dangky";
    }

    @GetMapping("/edit/{maDK}")
    public String showEditForm(@PathVariable("maDK") String maDK, Model model) {
        DangKyLamThem dangKyLamThem = dangKyLamThemService.getDangKyLamThemById(maDK);
        model.addAttribute("dangKyLamThem", dangKyLamThem);
        return "edit"; // Trả về template edit.html
    }

    @PostMapping("/update/{maDK}")
    public String updateDangKyLamThem(@PathVariable("maDK") String maDK, @ModelAttribute DangKyLamThem dangKyLamThem) {
        dangKyLamThemService.updateDangKyLamThem(maDK, dangKyLamThem);
        return "redirect:/dangky";
    }

    // Xóa đăng ký làm thêm
    @GetMapping("/delete/{maDK}")
    public String deleteDangKyLamThem(@PathVariable String maDK) {
        dangKyLamThemService.deleteDangKyLamThem(maDK);
        return "redirect:/dangky"; // Chuyển hướng về danh sách sau khi xóa
    }
}
