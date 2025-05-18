package com.dileep.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dileep.shopme.common.entity.Brand;
@Service
public class BrandServiceImpl implements IBrandService{

	@Autowired
	private IBrandRepository brandRepository;
	
	@Override
	public List<Brand> listAll() {
		return (List<Brand>) brandRepository.findAll();
	}

	
}
