package com.dileep.shopme.admin.brand;

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
import com.dileep.shopme.admin.brand.export.BrandCSVExporter;
import com.dileep.shopme.admin.category.ICategoryService;
import com.dileep.shopme.common.entity.Brand;
import com.dileep.shopme.common.entity.Category;

@Controller
public class BrandController {

	@Autowired
	private IBrandService brandService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/brands")
	public String listAll(@Param("sortDir") String sortDir  ,Model model) {
		
//		List<Brand> brands = brandService.listAll();
//		
//		model.addAttribute("brands", brands);
		return listByPage(1, sortDir, null, model);
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, @Param("sortDir") String sortDir,@Param("keyword") String keyword , Model model) {
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		BrandPageInfo pageInfo = new BrandPageInfo();
		List<Brand> brands = brandService.listByPage(pageInfo,pageNum,sortDir,keyword);
		long startCount  = (pageNum - 1)  * BrandServiceImpl.ROOT_BRAND_PER_PAGE + 1;
		long endCount = startCount + BrandServiceImpl.ROOT_BRAND_PER_PAGE - 1;
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
		
		model.addAttribute("brands",brands);
		model.addAttribute("reverseSortDir", reverseSortDir);
		return "brands/brands";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand ,@RequestParam("fileImage") MultipartFile multipartFile,RedirectAttributes ra)throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand savedBrand = brandService.save(brand);
			
			String uploadDir = "../brand-logos/"+savedBrand.getId();
			
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			brandService.save(brand);
		}
		ra.addFlashAttribute("message", "The brand has been saved successfully.");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		return "brands/brand_form";
	}
	
	
	@GetMapping("/brands/edit/{id}")
	public String editUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) throws BrandNotFoundException {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories); 
			model.addAttribute("pageTitle", "Edit Category(Id: " + id + ")");

			return "brands/brand_form";

		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String delete(@PathVariable(name = "id") Long id,Model model, RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/"+id;
			FileUploadUtil.removeDir(brandDir);
			
			redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully.");
		} catch (BrandNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
		List<Brand> brands  = brandService.listAll();
		BrandCSVExporter exporter = new BrandCSVExporter();
		exporter.export(brands, httpServletResponse);
	}
	
}
