package com.dileep.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dileep.shopme.common.entity.Product;

@Service
@Transactional
public class ProductServiceImpl implements IProductService{

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
}
