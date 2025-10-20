package com.dileep.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {

	@Autowired
	private ICustomerRepository customerRepository;
	
	@Autowired
	private TestEntityManager entityManager; 
	
	@Test
	public void testCreateCustomer1() {
	Long countryId = 106l; //USA
	Country country = entityManager.find(Country.class, countryId);
	
	Customer customer = new Customer();
	
	customer.setCountry(country);
	customer.setFirstName("Abhay");
	customer.setLastName("Kumar");
	customer.setPassword("Password1234");
	customer.setEmail("abhay.kumar@gmail.com");
	customer.setPhoneNumber("312-431-5442");
	customer.setAddressLine1("1929 West Drivee");
	customer.setCity("sacramento");
	customer.setState("California");
	customer.setPostalCode("95867");
	customer.setCreatedDate(new Date());
	
	Customer savedCustomer = customerRepository.save(customer);
	
	assertThat(savedCustomer).isNotNull();
	assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomer2() {
	Long countryId = 105l; //USA
	Country country = entityManager.find(Country.class, countryId);
	
	Customer customer = new Customer();
	
	customer.setCountry(country);
	customer.setFirstName("Naveen");
	customer.setLastName("Kumar");
	customer.setPassword("Password1234");
	customer.setEmail("naveen.kumar@gmail.com");
	customer.setPhoneNumber("909-431-5442");
	customer.setAddressLine1("1929 South Zone");
	customer.setCity("Madhurawada");
	customer.setState("Andhra Pradesh");
	customer.setPostalCode("530060");
	customer.setCreatedDate(new Date());
	
	Customer savedCustomer = customerRepository.save(customer);
	
	assertThat(savedCustomer).isNotNull();
	assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);
		assertThat(customers).hasSizeGreaterThan(0);
	}
	
	@Test
	public void testUpdateCustomer() {
		Long customerId = 1l;
		String lastName = "Vasupalli";
		Customer customer = customerRepository.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = customerRepository.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testGetCustomer() {
		Long customerId = 1l;
		Optional<Customer> findById = customerRepository.findById(customerId);
		
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Long customerId = 1l;
		customerRepository.deleteById(customerId);

		Optional<Customer> findById = customerRepository.findById(customerId);
		
		assertThat(findById).isNotPresent();
	}
	
	@Test
	public void testFindByEmail() {
		String email = "naveen.kumar@gmail.com";
		Customer customer = customerRepository.findByEmail(email);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code = "code_123";
		Customer customer = customerRepository.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testEnableCuustomer() {
		Long customerId = 2l;
		customerRepository.enable(customerId);
		Customer customer = customerRepository.findById(customerId).get();
		
		assertThat(customer.isEnabled()).isTrue();
	}
}
