package com.dileep.shopme.admin.brand;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.common.entity.Brand;
import com.dileep.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	@Autowired
	private IBrandRepository repository;
	
	@Test
	public void testCreateBrand1() {
		Category laptops = new Category(6l);
		Brand acer = new Brand("Acer");
		
		acer.getCategories().add(laptops);
		Brand savedBrand = repository.save(acer);
		
		assertThat(savedBrand).isNotNull();
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateBrand2() {
		Category cellPhones = new Category(4l);
		Category tablets = new Category(7l);

		Brand apple = new Brand("Apple");
		
		apple.getCategories().add(tablets);
		apple.getCategories().add(cellPhones);
		Brand savedBrand = repository.save(apple);
		
		assertThat(savedBrand).isNotNull();
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand3() {

		Brand samsung = new Brand("Samsung");
		
		samsung.getCategories().add(new Category(29l));
		samsung.getCategories().add(new Category(24l));
		Brand savedBrand = repository.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repository.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brands = repository.findById(1l).get();
		
		assertThat(brands.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repository.findById(3l).get();
		samsung.setName(newName);

		Brand savedBrand  = repository.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Long id = 2l;
		repository.deleteById(id);
		
		Optional<Brand> result = repository.findById(id);
		assertThat(result.isEmpty());
	}
}
