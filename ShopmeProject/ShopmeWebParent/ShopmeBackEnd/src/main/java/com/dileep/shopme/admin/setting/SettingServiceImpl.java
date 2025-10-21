package com.dileep.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dileep.shopme.common.entity.Setting;
import com.dileep.shopme.common.entity.SettingCategory;

@Service
public class SettingServiceImpl implements ISettingService{

	@Autowired
	private ISettingRepository settingRepository;
	
	@Override
	public List<Setting> listAllSettings(){
		return (List<Setting>) settingRepository.findAll();
	}

	@Override
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings  = new ArrayList<>();

		List<Setting> generalsettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		settings.addAll(generalsettings);
		settings.addAll(currencySettings);
		return new GeneralSettingBag(settings);
	}
	@Override
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}

	@Override
	public List<Setting> getMailServerSettings(){
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		
	}

	@Override
	public List<Setting> getMailTemplateSettings(){
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
		
	}
}
