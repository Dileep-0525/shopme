package com.dileep.shopme;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dileep.shopme.category.ICategoryService;
import com.dileep.shopme.common.entity.Category;

@Controller
public class MainControlle {

	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> listCategories = categoryService.listNoChildrenCategories();
		model.addAttribute("listCategories",listCategories);
		
		return "index";
	}
	
}
