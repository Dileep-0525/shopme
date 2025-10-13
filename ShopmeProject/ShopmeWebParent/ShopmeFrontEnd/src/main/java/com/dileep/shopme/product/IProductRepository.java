package com.dileep.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dileep.shopme.common.entity.Product;

public interface IProductRepository extends PagingAndSortingRepository<Product, Long>{

	@Query("SELECT p FROM Product p WHERE p.enabled=true AND "
			+ " (p.category.id = ?1 OR p. category.allParentIDs LIKE %?2%)"
			+ "ORDER BY p.name ASC")
	public Page<Product> listByCategory(Long categoryId, String categoryIDMatch,Pageable pageable);
	
	public Product findByAlias(String alias);

	@Query(value="SELECT * FROM products WHERE enabled=true AND "
			+ "MATCH(name,short_description,full_description) AGAINST(?1)",nativeQuery = true)
	public Page<Product> search(String keyword,Pageable pageable);
}
