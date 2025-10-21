package com.dileep.shopme.customer;

import java.util.List;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;

public interface ICustomerService {

	public List<Country> listAllCountries();

	public boolean isEmailUnique(String email);

	public void registerCustomer(Customer customer);

}
