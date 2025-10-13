package com.dileep.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dileep.shopme.category.ICategoryService;
import com.dileep.shopme.common.entity.Category;
import com.dileep.shopme.common.entity.Product;
import com.dileep.shopme.common.exception.CategoryNotFoundException;
import com.dileep.shopme.common.exception.ProductNotFoundException;


@Controller
public class ProductController {

	@Autowired
	private IProductService productService;
	
	@Autowired
	private ICategoryService categoryService;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewCategoryByPage(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,@PathVariable("pageNum") int pageNum , Model model) {
		try {
		Category category = categoryService.getCategory(alias);
		List<Category> listCategoryParents = categoryService.getCategoryParents(category);
		Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());
		List<Product> listProducts = pageProduct.getContent();
		
		long startCount  = (pageNum - 1)  * ProductServiceImpl.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductServiceImpl.PRODUCTS_PER_PAGE - 1;
				if(endCount > pageProduct.getTotalElements()) {
					endCount = pageProduct.getTotalElements();
				}
				
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",pageProduct.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems",pageProduct.getTotalElements());
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoryParents",listCategoryParents);
		model.addAttribute("listProducts",listProducts);
		model.addAttribute("category",category);
		
		return "product/product_by_category";
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{product_alias}")
	public String getMethodName(@PathVariable("product_alias") String alias,Model model) {
		try {
			Product product = productService.getProduct(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
			
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String searchFirstPage(@Param("keyword") String keyword,Model model) {
		return search(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String search(@Param("keyword") String keyword,@PathVariable("pageNum") int pageNum , Model model) {
		Page<Product> pageProducts = productService.search(keyword,pageNum);
		List<Product> listResult = pageProducts.getContent();
		
		long startCount  = (pageNum - 1)  * ProductServiceImpl.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductServiceImpl.SEARCH_RESULTS_PER_PAGE - 1;
				if(endCount > pageProducts.getTotalElements()) {
					endCount = pageProducts.getTotalElements();
				}
				
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems",pageProducts.getTotalElements());
		model.addAttribute("pageTitle", keyword + "-Search Result");
		
		model.addAttribute("keyword",keyword);
		model.addAttribute("listResult",listResult);
		return "product/search_result";
	}
	
	
}
