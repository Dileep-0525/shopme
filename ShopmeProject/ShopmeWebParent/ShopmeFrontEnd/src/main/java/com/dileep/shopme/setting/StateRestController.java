package com.dileep.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.State;
import com.dileep.shopme.common.entity.StateDTO;



@RestController
public class StateRestController {

	@Autowired
	private IStateRepository stateRepository;
	
	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Long id) {
		List<State> listStates = stateRepository.findByCountryOrderByNameAsc(new Country(id));
	List<StateDTO> result = new ArrayList<>();
	
	for(State state:listStates) {
		result.add(new StateDTO(state.getId(),state.getName()));
	}
		return result;
	}
	
}
