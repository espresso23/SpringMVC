package com.controller;

import com.model.ViecLam;
import com.service.ViecLamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vieclam")
public class ViecLamController {

    @Autowired
    private ViecLamService viecLamService;

    @GetMapping
    public String showPage(Model model) {
        model.addAttribute("viecLam", new ViecLam());
        model.addAttribute("danhSachViecLam", viecLamService.getAllViecLam()); // Thêm đối tượng rỗng cho form
        return "addViecLam";
    }

    @PostMapping("/addViecLam")
    public String addViecLam(@ModelAttribute("viecLam") ViecLam viecLam, RedirectAttributes redirectAttributes) {
        try {
            viecLamService.saveViecLam(viecLam);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm việc làm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi " + e.getMessage());

        }
        return "redirect:/vieclam";
    }

    @GetMapping("/edit/{maVL}")
    public String showEditViecLam(@org.springframework.web.bind.annotation.PathVariable("maVL") String maVL, Model model, RedirectAttributes redirectAttributes) {
        ViecLam viecLam = viecLamService.getViecLamById(maVL);
        if (viecLam == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy việc làm với mã: " + maVL);
            return "redirect:/vieclam";
        }
        model.addAttribute("viecLam", viecLam);
        return "editViecLam";
    }

    @PostMapping("/edit/{maVL}")
    public String updateViecLam(@org.springframework.web.bind.annotation.PathVariable("maVL") String maVL,
                                @ModelAttribute("viecLam") ViecLam viecLam,
                                RedirectAttributes redirectAttributes) {
        try {
            viecLamService.updateViecLam(maVL, viecLam);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật việc làm thành công!");
            return "redirect:/vieclam";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
            return "redirect:/vieclam/edit/" + maVL;
        }
    }

}
