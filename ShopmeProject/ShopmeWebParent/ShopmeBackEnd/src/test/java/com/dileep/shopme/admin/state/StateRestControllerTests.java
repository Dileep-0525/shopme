package com.dileep.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.dileep.shopme.admin.country.ICountryRepository;
import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ICountryRepository countryRepository;
	
	@Autowired
	IStateRepository stateRepository;
	
	@Test
	@WithMockUser(username="vasupallidileep525@gmail.com", password = "Dileep@2000" , roles = "ADMIN" )
	public void testListByCountries() throws JsonProcessingException,Exception {
		Long countryId = 1l;
		String url = "/states/list_by_country/"+countryId;

		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
	
		String jsonResponse = result.getResponse().getContentAsString();
		State[] states = objectMapper.readValue(jsonResponse, State[].class);
		assertThat(states).hasSizeGreaterThan(1);
		
	}
	
	@WithMockUser(username="vasupallidileep525@gmail.com", password = "Dileep@2000" , roles = "ADMIN" )
	public void testCreateState() throws JsonProcessingException,Exception {
		String url = "/states/save";
		Long countryId = 2l;
		Country country = countryRepository.findById(countryId).get();
		State state = new State("Arizona",country);
		
		 MvcResult result = mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(state))
					.with(csrf()))
				 .andDo(print())
				 .andExpect(status().isOk())
				 .andReturn();
	

		String response= result.getResponse().getContentAsString();
		Long stateId = Long.parseLong(response);
		
		Optional<State> findById = stateRepository.findById(stateId);
		
		assertThat(findById.isPresent());
	}
	
	@WithMockUser(username="vasupallidileep525@gmail.com", password = "Dileep@2000" , roles = "ADMIN" )
	public void testUpdateState() throws JsonProcessingException,Exception {
		String url = "/states/save";
		Long stateId = 9l;
		String stateName = "Alaska";
		State state = stateRepository.findById(stateId).get();

		mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state)).with(csrf())).andDo(print()).andExpect(status().isOk()).andExpect(content().string(String.valueOf(stateId)));
		
		Optional<State> findById = stateRepository.findById(stateId);
		assertThat(findById.isPresent());
		
		State updatedState = findById.get();
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	@Test
	@WithMockUser(username="vasupallidileep525@gmail.com", password = "Dileep@2000" , roles = "ADMIN" )
	public void testDeleteCountry() throws JsonProcessingException,Exception {
		Long stateId = 1l;
		String url = "/states/delete/" +stateId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());

		Optional<State> findById = stateRepository.findById(stateId);
		assertThat(findById).isNotPresent();
	}
}
