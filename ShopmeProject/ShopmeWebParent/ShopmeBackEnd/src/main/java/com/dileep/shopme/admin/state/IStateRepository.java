package com.dileep.shopme.admin.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.State;

public interface IStateRepository extends CrudRepository<State, Long>{

	public List<State> findByCountryOrderByNameAsc(Country country);
	
}
