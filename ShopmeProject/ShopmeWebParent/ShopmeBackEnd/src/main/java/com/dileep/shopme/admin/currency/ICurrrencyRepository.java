package com.dileep.shopme.admin.currency;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dileep.shopme.common.entity.Currency;

public interface ICurrrencyRepository extends CrudRepository<Currency, Long>{

	public List<Currency> findAllByOrderByNameAsc();
	
}
