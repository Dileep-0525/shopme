package com.dileep.shopme.product;

import org.springframework.data.domain.Page;

import com.dileep.shopme.common.entity.Product;
import com.dileep.shopme.common.exception.ProductNotFoundException;

public interface IProductService {

	public Page<Product> listByCategory(int pageNum, Long categoryId);

	public Product getProduct(String alias) throws ProductNotFoundException ;

	public Page<Product> search(String keyword, int pageNum);

}
