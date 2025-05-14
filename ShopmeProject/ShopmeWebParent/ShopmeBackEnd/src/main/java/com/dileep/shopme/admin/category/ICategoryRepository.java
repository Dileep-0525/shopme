package com.dileep.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dileep.shopme.common.entity.Category;

public interface ICategoryRepository extends PagingAndSortingRepository<Category, Long>{

	@Query("SELECT c FROM Category c WHERE c.parent.id is null ")
	public List<Category> findRootCategories(Sort sort);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is null ")
	public Page<Category> findRootCategories(Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
	public Page<Category> search(String keyword,Pageable pageable);
	
	public Category findByName(String name);

	public Category findByAlias(String alias);

	@Transactional
	@Modifying
	@Query("UPDATE  Category c SET c.enabled =?2 WHERE c.id= ?1")
	public void updateEnabledStatus(Long id, boolean enabled);

	public Long countById(Long id);
}
