package com.dileep.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

	@Autowired
	private IProductService productService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(@Param("id") Long id,@Param("name") String name) {
		return productService.checkUnique(id, name);
	}
	
}
