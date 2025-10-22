package com.dileep.shopme.setting;

import java.util.List;

import com.dileep.shopme.common.entity.Setting;
import com.dileep.shopme.common.entity.SettingBag;

public class EmailSettingBag extends SettingBag {

	public EmailSettingBag(List<Setting> listSetting) {
		super(listSetting);
	}

	public String getHost() {
		return super.getValue("MAIL_HOST");
	}
	public int getPort() {
		return Integer.parseInt(super.getValue("MAIL_PORT"));
	}
	
	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}

	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	
	public String getSmptAuth() {
		return super.getValue("SMTP_AUTH");
	}
	
	public String getSmptSecured() {
		return super.getValue("SMTP_SECURED");
	}
	
	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}
	
	public String getSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}
	
	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}
	
	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}
	
}
