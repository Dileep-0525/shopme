package com.dileep.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.common.entity.Setting;
import com.dileep.shopme.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

	@Autowired
	ISettingRepository settingRepository;
	
	@Test
	public void testCreateGeneralSettings() {
		Setting setting = new Setting("SITE_NAME","Shopeme",SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO","Shopeme.png",SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT","Copyright (C) 2021 Shopme Ltd.",SettingCategory.GENERAL);
		settingRepository.saveAll(List.of(setting,siteLogo,copyright));
		
		Iterable<Setting> iterable = settingRepository.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		
		settingRepository.saveAll(List.of(currencyId,symbol,symbolPosition,decimalDigits,decimalPointType,thousandsPointType));
		
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);
		
		settings.forEach(System.out::println);
	}
	
}
