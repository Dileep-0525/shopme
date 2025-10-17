package com.dileep.shopme.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dileep.shopme.common.entity.Setting;
import com.dileep.shopme.common.entity.SettingCategory;

public interface ISettingRepository extends CrudRepository<Setting, String>{

	public List<Setting> findByCategory(SettingCategory category);
	
}
