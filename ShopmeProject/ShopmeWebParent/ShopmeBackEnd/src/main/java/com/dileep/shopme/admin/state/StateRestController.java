package com.dileep.shopme.admin.state;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.State;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class StateRestController {

	@Autowired
	private IStateRepository stateRepository;
	
	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Long id) {
		List<State> listStates = stateRepository.findByCountryOrderByNameAsc(new Country(id));
	List<StateDTO> result = new ArrayList<>();
	
	for(State state:listStates) {
		result.add(new StateDTO(state.getId(),state.getName()));
	}
		return result;
	}
	
	@PostMapping("/states/save")
	public String postMethodName(@RequestBody State state) {
		State savedState = stateRepository.save(state);
		return String.valueOf(savedState.getId());
	}

	@DeleteMapping("/states/delete/{id}")
	public void getMethodName(@PathVariable Long id) {
		stateRepository.deleteById(id);
	}
	
}
