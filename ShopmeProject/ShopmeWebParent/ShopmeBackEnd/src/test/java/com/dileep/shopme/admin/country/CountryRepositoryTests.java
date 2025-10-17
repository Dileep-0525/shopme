package com.dileep.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {

	@Autowired
	private ICountryRepository countryRepository;
	
	@Test
	public void testCreateCountry() {
		Country country = countryRepository.save(new Country("China", "CN"));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCountries() {
		List<Country> listCountries = countryRepository.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);
				
		assertThat(listCountries.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCountry() {
		Long id = 1l;
		String name = "Republic of India";
		
		Country country = countryRepository.findById(id).get();
		country.setName(name);
		
		Country updatedCountry = countryRepository.save(country);
		
		assertThat(updatedCountry.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetCountry() {
		Long id=3l;
		Country country = countryRepository.findById(id).get();
		assertThat(country).isNotNull();
	}

	@Test
	public void testDeleteCountry() {
		Long id = 5l;
		countryRepository.deleteById(id);
		Optional<Country> findById = countryRepository.findById(id);
		assertThat(findById.isEmpty());
	}
}
