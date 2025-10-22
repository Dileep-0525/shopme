package com.dileep.shopme.setting;

import java.util.List;

import com.dileep.shopme.common.entity.Setting;

public interface ISettingService {

	public List<Setting> getGeneralSettings();

	public EmailSettingBag getEmailSettings();
	
}
