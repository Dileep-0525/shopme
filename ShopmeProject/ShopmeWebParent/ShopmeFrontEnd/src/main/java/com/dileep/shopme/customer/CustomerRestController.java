package com.dileep.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CustomerRestController {

	@Autowired
	private ICustomerService customerService;
	
	@PostMapping("/customers/check_unique_email")
	public String postMethodName(@Param("email") String email) {
		return customerService.isEmailUnique(email)? "OK": "Duplicated";
	}
	
	
}
