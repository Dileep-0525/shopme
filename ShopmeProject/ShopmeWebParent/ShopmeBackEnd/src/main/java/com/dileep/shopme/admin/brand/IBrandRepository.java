package com.dileep.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dileep.shopme.common.entity.Brand;

public interface IBrandRepository extends PagingAndSortingRepository<Brand, Long>{

	public Long countById(Long id);

	public Brand findByName(String name);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);

	@Query("SELECT COUNT(b) FROM Brand b JOIN b.categories c WHERE c.id = :categoryId")
	public int getCount(@Param("categoryId") Long categoryId);
	
	@Query("SELECT NEW Brand(b.id,b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();
}
