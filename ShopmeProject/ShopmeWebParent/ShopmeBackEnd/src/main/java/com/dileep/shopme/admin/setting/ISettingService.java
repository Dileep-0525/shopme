package com.dileep.shopme.admin.setting;

import java.util.List;

import com.dileep.shopme.common.entity.Setting;

public interface ISettingService {

	public List<Setting> listAllSettings();

	public GeneralSettingBag getGeneralSettings();

	public void saveAll(Iterable<Setting> settings);
	
}
