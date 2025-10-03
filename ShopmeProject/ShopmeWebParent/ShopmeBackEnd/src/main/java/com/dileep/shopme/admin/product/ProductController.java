package com.dileep.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.dileep.shopme.admin.brand.IBrandService;
import com.dileep.shopme.admin.category.ICategoryService;
import com.dileep.shopme.admin.security.ShopmeUserDetails;
import com.dileep.shopme.common.entity.Brand;
import com.dileep.shopme.common.entity.Category;
import com.dileep.shopme.common.entity.Product;
import com.dileep.shopme.common.entity.ProductImage;


@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	
	@Autowired
	private IBrandService brandService;
	
	@Autowired
	private IProductService productService;

	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
	return listByPage(1	, model ,"name", "asc", null,0l);
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		Long numberOfExistingExtraImages = product.getImages() != null ? product.getImages().size() : 0l;

		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product",product);
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("pageTitle","Create New Product");
		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
			@RequestParam(value="fileImage" ,required = false) MultipartFile mainImageMultipart,
			@RequestParam(value= "extraImage" ,required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name="detailIDs",required = false) String[] detailIDs,
			@RequestParam(name="detailNames",required = false) String[] detailNames,
			@RequestParam(name="detailValues",required = false) String[] detailValues,
			@RequestParam(name="imageIDs",required = false) String[] imageIDs,
			@RequestParam(name="imageNames",required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {

		if(loggedUser.hasRole("SalesPerson")) {
			productService.saveProductPrice(product);
			redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
			return "redirect:/products";
		}
		
		setMainImageName(mainImageMultipart,product);
		setExistingExtraImageNames(imageIDs,imageNames,product);
		setNewExtraImageNames(extraImageMultiparts,product);
		setProductDetails(detailIDs,detailNames,detailValues,product);
		
		Product savedProduct = productService.save(product);
		
		saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);
		
		deleteExtraImagesWeredRemovedOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
		return "redirect:/products";
	}
	
	private void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/" +product.getId() + "/extras" ;
		Path direPath = Paths.get(extraImageDir);
		try {
			Files.list(direPath).forEach(file ->{
				String filename = file.toFile().getName();
				if(!product.containsImageName(filename)) {
					try {
						Files.delete(file);
					} catch (Exception e) {
						LOGGER.error("Could not delete extra image: " +file);
					}
					
				}
				
			});
		} catch (IOException e) {
			LOGGER.error("Could not list directory: " +direPath);
		}
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		if(imageIDs == null || imageIDs.length==0)return;
		Set<ProductImage> productImages = new HashSet<>();
		for(int count=0; count< imageIDs.length;count++) {
			Long id = Long.parseLong(imageIDs[count]);
			String name = imageNames[count];
			productImages.add(new ProductImage(id, name, product));
		}
		product.setImages(productImages);
	}

	private void setProductDetails(String[] detailIDs,String[] detailNames,String[] detailValues ,Product product) {
		if(detailNames ==null || detailNames.length ==0) return;
		
		for(int count=0;count<detailNames.length;count++) {
			String name= detailNames[count];
			String value = detailValues[count];
			Long id = Long.parseLong(detailIDs[count]);
			
			if(id!=0) {
				product.addDetail(id,name, value);
			}else if(!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
		
	}
	
	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {
		if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
		String uploadDir = "../product-images/"+savedProduct.getId();
		
		FileUploadUtil.clearDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		if(extraImageMultiparts.length>0) {
			String uploadDir = "../product-images/"+savedProduct.getId() +"/extras";
			for(MultipartFile multipartFile:extraImageMultiparts) {
				if(multipartFile.isEmpty())continue;
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				}
			}
		}

	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if(extraImageMultiparts.length>0) {
			for(MultipartFile multipartFile:extraImageMultiparts) {
				if(!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if(!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
		
	}

	private void setMainImageName(MultipartFile mainImageMultipart,Product product) {
		if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
		
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Long id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		productService.updateEnabledStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disabled";
		redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been " + status + ".");
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/"+id+"/extras";
			String productImagesDir = "../product-images/"+id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully.");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Long id, Model model,RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Long numberOfExistingExtraImages = product.getImages() != null ? product.getImages().size() : 0l;
			model.addAttribute("product",product);
			model.addAttribute("listBrands",listBrands);
			model.addAttribute("pageTitle","Edit Product(ID: " +id+")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			return "products/product_form";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum,Model model,
			@Param("sortField") String sortField , @Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId") Long categoryId ) {
		System.out.println(categoryId+"categoryId");
		Page<Product> page = productService.listByPage(pageNum,sortField,sortDir,keyword,categoryId);
		List<Product> listProducts = page.getContent();
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		long startCount  = (pageNum - 1)  * ProductServiceImpl.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductServiceImpl.PRODUCTS_PER_PAGE - 1;
				if(endCount > page.getTotalElements()) {
					endCount = page.getTotalElements();
				}
				
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;
		
		if(categoryId !=null) model.addAttribute("categoryId", categoryId);
		
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("keyword",keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		model.addAttribute("listProducts",listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		return "products/products";
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Long id, Model model,RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product",product);
			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/products";
		}
	}
	
}
