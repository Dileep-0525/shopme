package com.dileep.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dileep.shopme.common.entity.Product;
import com.dileep.shopme.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductServiceImpl implements IProductService{

	public static final int PRODUCTS_PER_PAGE = 5;
	
	@Autowired
	private IProductRepository productRepository;
	
	@Override
	public List<Product> listAll(){
		return (List<Product>) productRepository.findAll();
	}
	
	@Override
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if(product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		}else {
			product.setAlias(product.getAlias().replace(" ", "-"));
		}
		product.setUpdatedTime(new Date());
		
		return productRepository.save(product);
	}
	
	@Override
	public void saveProductPrice(Product productInForm) {
		Product productInDB = productRepository.findById(productInForm.getId()).get();
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());
		productRepository.save(productInDB);
	}
	
	@Override
	public String checkUnique(Long id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = productRepository.findByName(name);
		
		if(isCreatingNew) {
			if(productByName != null) return "Duplicate";
		} else {
			if(productByName != null && productByName.getId() != id) return "Duplicate";
		}
		
		return "OK";
	}
	
	@Override
	public void updateEnabledStatus(Long id, boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
	}
	
	@Override
	public void delete(Long id) {
		Long countById = productRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new IllegalArgumentException("Product with ID " + id + " does not exist.");
		}
		productRepository.deleteById(id);
	}
	
	@Override
	public Product get(Long id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any product with ID "+id);
		}
	}
	
	@Override
	public Page<Product> listByPage(int pageNum,String sortField,String sortDir,String keyword,Long categoryId){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable =  PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE ,sort);
		if(keyword!=null && !keyword.isEmpty()) {
			if(categoryId != null && categoryId>0) {
				String categoryInMatch = "-"+ String.valueOf(categoryId)+"-";
				return productRepository.findAllInCategory(categoryId, categoryInMatch, pageable);
			}
			return productRepository.findAll(keyword, pageable);
		}
		if(categoryId != null && categoryId>0) {
			String categoryInMatch = "-"+ String.valueOf(categoryId)+"-";
			return productRepository.findAllInCategory(categoryId, categoryInMatch, pageable);
		}
		return productRepository.findAll(pageable);
	}
	
}
