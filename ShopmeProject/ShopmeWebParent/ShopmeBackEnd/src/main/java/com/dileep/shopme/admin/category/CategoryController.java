package com.dileep.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dileep.shopme.admin.FileUploadUtil;
import com.dileep.shopme.admin.category.export.CategoryCSVExporter;
import com.dileep.shopme.admin.user.UserNotFoundException;
import com.dileep.shopme.admin.user.export.UserCSVExporter;
import com.dileep.shopme.common.entity.Category;
import com.dileep.shopme.common.entity.User;
@Controller
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir  ,Model model) {
		return listByPage(1, sortDir, null,model);
	}
	
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, @Param("sortDir") String sortDir,@Param("keyword") String keyword  ,Model model) {
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = categoryService.listByPage(pageInfo,pageNum,sortDir,keyword);
		long startCount  = (pageNum - 1)  * CategoryServiceImpl.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryServiceImpl.ROOT_CATEGORIES_PER_PAGE - 1;
				if(endCount > pageInfo.getTotalElements()) {
					endCount = pageInfo.getTotalElements();
				}
				
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages",pageInfo.getTotalPages());
		model.addAttribute("totalItems",pageInfo.getTotalElements());
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("sortField","name");
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("keyword",keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> categories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categories);

		model.addAttribute("pageTitle","Create New Category");
		return "categories/category_form";
		
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,@RequestParam("fileImage") MultipartFile multipartFile , RedirectAttributes redirectAttributes) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/"+savedCategory.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The Category has been saved successfully");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Category category = categoryService.get(id);
//			List<Category> categories = categoryService.listAll("asc");
			model.addAttribute("category", category);
//			model.addAttribute("categories", categories); 
			model.addAttribute("pageTitle", "Edit Category(Id: " + id + ")");

			return "categories/category_form";

		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateUserEnableStatus(@PathVariable(name = "id") Long id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {

		categoryService.updateUserenableStatus(id, enabled);
		String status = enabled ? "enabled" : "Disabled";
		 
		String message = "The Category ID " + id + " has been " + status ;
		
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/categories";
	}
	
	
	@GetMapping("/categories/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message", "The Category Id " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());

		}
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
		List<Category> categories = categoryService.listAll("asc");
		CategoryCSVExporter exporter = new CategoryCSVExporter();
		exporter.export(categories, httpServletResponse);
	}
	
}
