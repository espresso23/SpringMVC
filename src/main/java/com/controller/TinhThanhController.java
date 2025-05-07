package com.controller;

import com.model.TinhThanh;
import com.service.TinhThanhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tinh-thanh")
public class TinhThanhController {
    private final TinhThanhService tinhThanhService;

    @Autowired
    public TinhThanhController(TinhThanhService tinhThanhService) {
        this.tinhThanhService = tinhThanhService;
    }

    @GetMapping("/danh-sach")
    public String listTinhThanh(Model model) {
        model.addAttribute("tinhThanhList", tinhThanhService.getAllTinhThanh());
        return "tinh-thanh/danh-sach";
    }

    @GetMapping("/them-moi")
    public String showAddForm(Model model) {
        model.addAttribute("tinhThanh", new TinhThanh());
        return "tinh-thanh/them-moi";
    }

    @PostMapping("/them-moi")
    public String addTinhThanh(@ModelAttribute("tinhThanh") TinhThanh tinhThanh,
                              RedirectAttributes redirectAttributes) {

        TinhThanh tinhThanhExists = tinhThanhService.getTinhThanhById(tinhThanh.getMaTinhThanh());
        if (tinhThanhExists!= null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tỉnh thành đã tồn tại!");
            return "redirect:/tinh-thanh/them-moi";
        }
        try {
            tinhThanhService.addTinhThanh(tinhThanh);
            redirectAttributes.addFlashAttribute("success", "Thêm tỉnh thành thành công!");
            return "redirect:/tinh-thanh/danh-sach";
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
            return "tinh-thanh/them-moi";
        }
    }

    @GetMapping("/chinh-sua/{maTinhThanh}")
    public String showEditForm(@PathVariable String maTinhThanh, Model model) {
        System.out.println("==> [TinhThanhController] GET /chinh-sua/" + maTinhThanh);
        TinhThanh tinhThanh = tinhThanhService.getTinhThanhById(maTinhThanh);
        if (tinhThanh == null) {
            System.out.println("==> [TinhThanhController] Không tìm thấy tỉnh thành: " + maTinhThanh);
        }
        model.addAttribute("tinhThanh", tinhThanh);
        return "tinh-thanh/chinh-sua";
    }

    @PostMapping("/chinh-sua/{maTinhThanh}")
    public String updateTinhThanh(@PathVariable String maTinhThanh,
                                 @ModelAttribute("tinhThanh") TinhThanh tinhThanh,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tinh-thanh/chinh-sua";
        }

        try {
            tinhThanhService.updateTinhThanh(maTinhThanh, tinhThanh);
            redirectAttributes.addFlashAttribute("success", "Cập nhật tỉnh thành thành công!");
            return "redirect:/tinh-thanh/danh-sach";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/tinh-thanh/chinh-sua/" + maTinhThanh;
        }
    }

    @GetMapping("/xoa/{maTinhThanh}")
    public String deleteTinhThanh(@PathVariable String maTinhThanh,
                                 RedirectAttributes redirectAttributes) {
        try {
            tinhThanhService.deleteTinhThanh(maTinhThanh);
            redirectAttributes.addFlashAttribute("success", "Xóa tỉnh thành thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tinh-thanh/danh-sach";
    }
} 