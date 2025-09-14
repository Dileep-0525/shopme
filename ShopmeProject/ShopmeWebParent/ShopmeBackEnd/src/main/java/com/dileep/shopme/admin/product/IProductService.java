package com.dileep.shopme.admin.product;

import java.util.List;

import com.dileep.shopme.common.entity.Product;

public interface IProductService {

	public List<Product> listAll();

	public Product save(Product product);

	public String checkUnique(Long id, String name);
	
	public void updateEnabledStatus(Long id, boolean enabled);
	
	public void delete(Long id);

	public Product get(Long id) throws ProductNotFoundException;
}
