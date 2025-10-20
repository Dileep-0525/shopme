package com.dileep.shopme.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dileep.shopme.common.entity.Country;

public interface ICountryRepository extends CrudRepository<Country, Long> {

	public List<Country> findAllByOrderByNameAsc();
	
}
