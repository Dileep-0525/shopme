package com.dileep.shopme.admin.product;

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
}
