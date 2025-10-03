package com.dileep.shopme.admin.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dileep.shopme.common.entity.Product;

public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {

	public Product findByName(String name); 
	
	@Modifying
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	public void updateEnabledStatus(Long id, boolean enabled);

	public Long countById(Long id);

	@Query("SELECT p FROM Product p where p.name LIKE %?1%"
			+ "OR p.shortDescription LIKE %?1% "
			+ "OR p.fullDescription LIKE %?1% "
			+ "OR p.brand.name LIKE %?1% "
			+ "OR p.category.name LIKE %?1% ")
	public Page<Product> findAll(String keyword,Pageable pageable);

	@Query("SELECT p From Product p WHERE p.category.id =:categoryId "
			+ "OR p.category.allParentIDs LIKE %:categoryIdMatch%")
	public Page<Product> findAllInCategory(Long categoryId,String categoryIdMatch,Pageable pageable);

	@Query("SELECT p From Product p WHERE (p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%) AND"
			+ "( p.name LIKE %?3%"
			+ "OR p.shortDescription LIKE %?3% "
			+ "OR p.fullDescription LIKE %?3% "
			+ "OR p.brand.name LIKE %?3% "
			+ "OR p.category.name LIKE %?3% )" )
	public Page<Product> searchInCategory(Long categoryId,String categoryIdMatch,String keyword,Pageable pageable);
	
}
