package com.dileep.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

	@Autowired
	private ICategoryService categoryService;
	
	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Long id,@Param("name") String name,@Param("alias") String alias) {
		return categoryService.checkUnique(id, name, alias);
	}
	
}
