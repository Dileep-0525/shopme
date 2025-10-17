package com.dileep.shopme.setting;

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
	public List<Setting> getGeneralSettings() {
		return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
//	@Override
//	public void saveAll(Iterable<Setting> settings) {
//		settingRepository.saveAll(settings);
//	}
}
