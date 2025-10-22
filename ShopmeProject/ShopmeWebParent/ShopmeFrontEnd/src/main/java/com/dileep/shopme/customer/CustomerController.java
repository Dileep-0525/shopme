package com.dileep.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dileep.shopme.Utility;
import com.dileep.shopme.common.entity.Country;
import com.dileep.shopme.common.entity.Customer;
import com.dileep.shopme.setting.EmailSettingBag;
import com.dileep.shopme.setting.ISettingService;
import com.dileep.shopme.setting.SettingFilter;
import com.dileep.shopme.setting.SettingServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CustomerController {

    private final SettingFilter settingFilter;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISettingService settingService;

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
	
	@PostMapping("/create_customer")
	public String postMethodName( Customer customer,Model model , HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request,customer);
		
		model.addAttribute("pageTitle","Registration Succeeded!");
		return "/register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		System.out.println("emailSettings From Address" +emailSettings.getFromAddress());
		
		JavaMailSenderImpl mailSenderImpl = Utility.prepareMailSender(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFirstName());
		
		String verifyURL = Utility.getSiteURL(request) + "/verify?code="+ customer.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		System.out.println(content);
		
		helper.setText(content,true);
		
		mailSenderImpl.send(message);
		
		System.out.println("to Address" +toAddress);
		System.out.println("Verify URL: " +verifyURL );
	}
	
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean verified = customerService.verify(code);
		
		return "register/" +(verified ? "verify_success" : "verify_fail");
	}
	
}
