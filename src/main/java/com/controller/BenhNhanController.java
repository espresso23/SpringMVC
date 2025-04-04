package com.controller;

// Import required classes and annotations
import com.model.*;
import com.service.BenhNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/benhnhan") // Base URL for handling requests related to BenhNhan
public class BenhNhanController {
    private BenhNhanService benhNhanService;

    // Constructor-based dependency injection
    @Autowired
    public BenhNhanController(BenhNhanService benhNhanService) {
        this.benhNhanService = benhNhanService;
    }

    // Display the form to add a new BenhNhan
    @GetMapping("")
    public String showPageBenhNhan(Model model) {
        model.addAttribute("benhNhan", new BenhNhan()); // Add an empty BenhNhan object to the model
        return "addBenhNhan"; // Return the view for adding BenhNhan
    }

    // Handle form submission for adding a new BenhNhan
    @PostMapping("/addBenhNhan")
    public String addBenhNhan(@ModelAttribute("benhNhan") BenhNhan benhNhan, RedirectAttributes redirectAttributes) {
        try {
            benhNhanService.createBenhNhan(benhNhan); // Save BenhNhan to the database
            redirectAttributes.addFlashAttribute("successMessage", "Add Benh Nhan successfully");
            return "redirect:/benhnhan/list"; // Redirect to the list page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // Handle errors
            return "redirect:/benhnhan"; // Redirect back to the form page
        }
    }

    // Display the list of BenhNhan
    @GetMapping("/list")
    public String showListBenhNhan(Model model) {
        List<BenhNhan> listBenhNhan = benhNhanService.getAllBenhNhan(); // Fetch all BenhNhan
        model.addAttribute("listBenhNhan", listBenhNhan); // Add list to the model
        return "listBenhNhan"; // Return the view for displaying the list
    }

    // Display search results (optional)
    @GetMapping("/listsearch")
    public String showListBenhNhansearh(Model model, RedirectAttributes redirectAttributes) {
        List<BenhNhan> listBenhNhan = benhNhanService.getAllBenhNhan(); // Fetch all BenhNhan
        redirectAttributes.addFlashAttribute("listBenhNhan", listBenhNhan); // Pass the list as flash attributes
        return "listBenhNhan"; // Return the view for displaying the list
    }

    // Search BenhNhan by "soCMND"
    @GetMapping("/search")
    public String searchByCMND(@RequestParam("soCMND") String soCMND,
                                     RedirectAttributes redirectAttributes) {
        List<BenhNhan> benhNhanList = benhNhanService.getBenhNhansBySoCMND(soCMND.trim()); // Search for BenhNhan

        if (benhNhanList.isEmpty()) {
            redirectAttributes.addFlashAttribute("notFoundMessage", "Không tìm thấy bệnh nhân với số: '" + soCMND + "'"); // Handle not found case
        } else {
            redirectAttributes.addFlashAttribute("listBenhNhan", benhNhanList); // Pass results
            redirectAttributes.addFlashAttribute("searchKeyword", soCMND); // Save search keyword
        }
        return "redirect:/benhnhan/listsearch"; // Redirect to the search results page
    }

    //show form edit benh nhan
    @GetMapping("/edit/{maBenhNhan}")
    public String showFormEdit(@PathVariable("maBenhNhan") String maBenhNhan, Model model, RedirectAttributes redirectAttributes) {
        BenhNhan benhNhan = benhNhanService.getBenhNhanById(maBenhNhan);

        // In ra console để kiểm tra dữ liệu
        System.out.println("Bệnh nhân được tìm thấy: " + benhNhan);

        if (benhNhan == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bệnh nhân không tồn tại.");
            return "redirect:/benhnhan/list";
        }

        model.addAttribute("benhNhan", benhNhan);
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
}