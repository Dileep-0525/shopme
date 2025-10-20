package com.dileep.shopme.customer;

import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;
import com.dileep.shopme.setting.SettingFilter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CustomerController {

    private final SettingFilter settingFilter;

	@Autowired
	private ICustomerService customerService;

    CustomerController(SettingFilter settingFilter) {
        this.settingFilter = settingFilter;
    }
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer",new Customer());
		
		return "register/register_form";
	}
	
	
}
