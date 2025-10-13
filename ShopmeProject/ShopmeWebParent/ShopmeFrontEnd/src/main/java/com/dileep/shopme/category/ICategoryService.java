package com.dileep.shopme.category;

import java.util.List;

import com.dileep.shopme.common.entity.Category;
import com.dileep.shopme.common.exception.CategoryNotFoundException;

public interface ICategoryService {

	public List<Category> listNoChildrenCategories();

	public Category getCategory(String alias)  throws CategoryNotFoundException;

	public List<Category> getCategoryParents(Category child);
	
}
