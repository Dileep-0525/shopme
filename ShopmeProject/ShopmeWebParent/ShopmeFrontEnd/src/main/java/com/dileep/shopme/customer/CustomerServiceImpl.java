package com.dileep.shopme.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;
import com.dileep.shopme.setting.ICountryRepository;

@Service
public class CustomerServiceImpl implements ICustomerService{

	@Autowired
	private ICountryRepository countryRepository;
	
	@Autowired
	private ICustomerRepository customerRepository;
	
	@Override
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	@Override
	public boolean isEmailUnique(String email) {
		Customer customer  = customerRepository.findByEmail(email);
		return customer == null;
	}
	
}
