package com.dileep.shopme.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;

@Controller
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer",new Customer());
		
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String postMethodName( Customer customer,Model model) {
		customerService.registerCustomer(customer);
		
		model.addAttribute("pageTitle","Registration Succeeded!");
		return "/register/register_success";
	}
	
}
