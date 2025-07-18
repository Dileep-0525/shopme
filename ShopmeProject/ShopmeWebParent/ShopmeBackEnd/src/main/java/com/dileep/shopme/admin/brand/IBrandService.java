package com.dileep.shopme.admin.brand;

import java.util.List;

import com.dileep.shopme.common.entity.Brand;

public interface IBrandService {

	public List<Brand> listAll();

	public Brand get(Long id) throws BrandNotFoundException;

	public void delete(Long id) throws BrandNotFoundException;

	public Brand save(Brand brand);

	public String checkUnique(Long id, String name);

	public List<Brand> listByPage(BrandPageInfo pageInfo, int pageNum, String sortDir, String keyword);

}
