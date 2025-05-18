package com.dileep.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dileep.shopme.common.entity.Brand;

@Controller
public class BrandController {

	@Autowired
	private IBrandService brandService;
	
	@GetMapping("/brands")
	public String listAll(Model model) {
		
		List<Brand> brands = brandService.listAll();
		
		model.addAttribute("brands", brands);
		return "brands/brands";
	}
}
