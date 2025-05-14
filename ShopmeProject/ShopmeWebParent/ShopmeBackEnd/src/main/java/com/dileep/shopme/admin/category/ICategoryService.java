package com.dileep.shopme.admin.category;

import java.util.List;

import com.dileep.shopme.admin.user.UserNotFoundException;
import com.dileep.shopme.common.entity.Category;

public interface ICategoryService {

	public List<Category> listAll(String sortDir);
	
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber,String sortDir,String keyword);

	public List<Category> listCategoriesUsedInForm();

	public Category save(Category category);

	public Category get(Long id) throws UserNotFoundException;

	public String checkUnique(Long id, String name, String alias);

	public void updateUserenableStatus(Long id, boolean enabled);

	public void delete(Long id) throws UserNotFoundException;

}
