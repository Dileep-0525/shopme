package com.dileep.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dileep.shopme.common.entity.Product;
import com.dileep.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductServiceImpl implements IProductService{

	public static final int PRODUCTS_PER_PAGE = 10;
	public static final int SEARCH_RESULTS_PER_PAGE = 10;
	
	@Autowired
	private IProductRepository productRepository;
	
	@Override
	public Page<Product> listByCategory(int pageNum,Long categoryId){
		String categoryInMatch = "-"+String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		return productRepository.listByCategory(categoryId, categoryInMatch, pageable);
	}
	
	@Override
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = productRepository.findByAlias(alias);
		if(product==null) {
			throw new ProductNotFoundException("Could not find any product with alias " +alias);
		}
		return product;
	}
	
	@Override
	public Page<Product> search(String keyword,int pageNum){
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		return productRepository.search(keyword, pageable);
	} 
	
}
