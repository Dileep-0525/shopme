package com.dileep.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.dileep.shopme.common.entity.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//@DataJpaTest
@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ICountryRepository entityManager;
	
	@Test
	@WithMockUser(username="vasupallidileep525@gmail.com", password = "Dileep@2000" , roles = "ADMIN" )
	public void testListCountries() throws Exception {
		String url ="/countries/list";
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
		
		for(Country country:countries) {
			System.out.println(country);
		}
		
		assertThat(countries).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username="vasupallidileep525@gmail.com", password = "Dileep@2000" , roles = "ADMIN" )
	public void testCreateCountry() throws JsonProcessingException,Exception {
		String url = "/countries/save";
		String countryName = "Canada";
		String countryCode = "CA";
		Country country = new Country(countryName,countryCode);
		
		 MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country)).with(csrf())).andDo(print()).andExpect(status().isOk()).andReturn();
	
		String response = result.getResponse().getContentAsString();

		Long countryId = Long.parseLong(response);
		Optional<Country> findById = entityManager.findById(countryId);
		assertThat(findById.isPresent());
		
		Country savedCountry = entityManager.findById(countryId).get();
		
		assertThat(savedCountry.getName()).isEqualTo(savedCountry.getName());
	}
	
		
	
}
