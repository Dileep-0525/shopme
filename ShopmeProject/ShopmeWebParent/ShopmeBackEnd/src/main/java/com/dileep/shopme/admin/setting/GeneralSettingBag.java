package com.dileep.shopme.admin.setting;

import java.util.List;

import com.dileep.shopme.common.entity.Setting;
import com.dileep.shopme.common.entity.SettingBag;

public class GeneralSettingBag extends SettingBag{

	public GeneralSettingBag(List<Setting> listSetting) {
		super(listSetting);
	}
	
	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}

	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
	
}
