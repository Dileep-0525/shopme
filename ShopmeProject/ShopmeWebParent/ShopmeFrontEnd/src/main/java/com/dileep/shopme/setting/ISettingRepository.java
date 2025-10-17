package com.dileep.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dileep.shopme.common.entity.Setting;
import com.dileep.shopme.common.entity.SettingCategory;

public interface ISettingRepository extends CrudRepository<Setting, String>{

	public List<Setting> findByCategory(SettingCategory category);
	
	@Query("SELECT s FROM Setting s WHERE s.category= ?1 or s.category= ?2 ")
	public List<Setting> findByTwoCategories(SettingCategory categoryOne, SettingCategory categoryTwo);
	
}
