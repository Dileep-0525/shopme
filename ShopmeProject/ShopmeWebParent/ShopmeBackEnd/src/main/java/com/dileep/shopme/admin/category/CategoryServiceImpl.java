package com.dileep.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dileep.shopme.admin.user.UserNotFoundException;
import com.dileep.shopme.common.entity.Category;

@Service
public class CategoryServiceImpl implements ICategoryService {

	public static final int ROOT_CATEGORIES_PER_PAGE = 4;
	
	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	public List<Category> listAll(String sortDir) {
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else if(sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		List<Category> rootCategories = categoryRepository.findRootCategories(sort);
		return listHeirarchicalCategories(rootCategories,sortDir);
	}

	private List<Category> listHeirarchicalCategories(List<Category> rootCategories , String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));

				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1,sortDir);
			}
		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> heirarchicalCategories, Category parent, int subLevel , String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(),sortDir);
		int newSubLevel = subLevel + 1;
		for (Category subCategory : children) {

			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			heirarchicalCategories.add(Category.copyFull(subCategory, name));

			listSubHierarchicalCategories(heirarchicalCategories, subCategory, newSubLevel,sortDir);

		}
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = category.getChildren();
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}
		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	@Override
	public Category get(Long id) throws UserNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
	}

	@Override
	public String checkUnique(Long id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		Category categoryByName = categoryRepository.findByName(name);

		if (isCreatingNew) {
			if(categoryByName != null) {
				return "DuplicateName";
			}else {
				Category categoryByAlias = categoryRepository.findByAlias(alias);
				if(categoryByAlias!=null) {
					return "DuplicateAlias";
				}
			}
		}else {
			if(categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryByAlias = categoryRepository.findByAlias(alias);
			if(categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}

		return "OK";
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children ){
		return sortSubCategories(children, "asc");
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children , String sortDir){
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				if(sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				}else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});
		
		sortedChildren.addAll(children);
		return sortedChildren;
	}

	@Override
	public void updateUserenableStatus(Long id, boolean enabled) {
		categoryRepository.updateEnabledStatus(id, enabled);
	}
	
	@Override
	public void delete(Long id) throws UserNotFoundException {
		Long countById = categoryRepository.countById(id);
		
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
		categoryRepository.deleteById(id);
	}
	
	@Override
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber,String sortDir,String keyword) {
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else if(sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		Pageable pageable =  PageRequest.of(pageNumber-1, ROOT_CATEGORIES_PER_PAGE,sort);

		Page<Category> pageCategories  = null;
		
		if(keyword!=null && !keyword.isEmpty()) {
			pageCategories = categoryRepository.search(keyword, pageable);
		}else {
			pageCategories = categoryRepository.findRootCategories(pageable);
		}
		
		List<Category> rootCategories= pageCategories.getContent();
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		if(keyword!=null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			for(Category category : searchResult) {
				category.setHasChildren(category.getChildren().size()>0);
			}
			return searchResult;
		}else {
			return listHeirarchicalCategories(rootCategories,sortDir);	
		}
	}
	
	
	
}
