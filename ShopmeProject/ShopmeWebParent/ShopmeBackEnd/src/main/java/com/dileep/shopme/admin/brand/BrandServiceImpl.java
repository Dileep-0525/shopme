package com.dileep.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dileep.shopme.common.entity.Brand;
@Service
public class BrandServiceImpl implements IBrandService{

	
	public static final int ROOT_BRAND_PER_PAGE = 5;
	
	@Autowired
	private IBrandRepository brandRepository;
	
	@Override
	public List<Brand> listAll() {
		return (List<Brand>) brandRepository.findAll();
	}
	
	@Override
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}

	@Override
	public Brand get(Long id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any Brand with ID" + id);
		}
	}
	
	@Override
	public void delete(Long id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);
		if(countById == null || countById ==0) {
			throw new BrandNotFoundException("Could not find any Brand with ID" + id);
		}
		brandRepository.deleteById(id);
	}

	@Override
	public String checkUnique(Long id,String name) {
		boolean isCreatingNew = (id==null || id==0);
		Brand brandByName = brandRepository.findByName(name);
		
		if(isCreatingNew) {
			if(brandByName != null) return "Duplicate";
		}else {
			if(brandByName !=null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "OK";
	}

	@Override
	public List<Brand> listByPage(BrandPageInfo pageInfo, int pageNum, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
		if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else if(sortDir.equals("desc")) {
			sort = sort.descending();
		}
		Page<Brand> pageCategories  = null;
		sort = sortDir.equals("asc")? sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum -1, ROOT_BRAND_PER_PAGE ,sort);
		
		if(keyword!=null) {
			pageCategories = brandRepository.findAll(keyword, pageable);
		}else {
			pageCategories=brandRepository.findAll(pageable);
		}
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		
		return pageCategories.getContent();
	}

}
