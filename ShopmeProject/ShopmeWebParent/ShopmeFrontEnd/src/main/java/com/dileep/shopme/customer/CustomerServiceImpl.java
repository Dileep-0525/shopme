package com.dileep.shopme.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;
import com.dileep.shopme.setting.ICountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService{

	@Autowired
	private ICountryRepository countryRepository;
	
	@Autowired
	private ICustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	@Override
	public boolean isEmailUnique(String email) {
		Customer customer  = customerRepository.findByEmail(email);
		return customer == null;
	}
	
	@Override
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedDate(new Date());
		
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
		
		System.out.println("verification Code:" +randomCode);
		
		customerRepository.save(customer);
	}
	
	public void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
	
	@Override
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);
		
		if(customer == null || customer.isEnabled()) {
			return false;
		}else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}
	
}
