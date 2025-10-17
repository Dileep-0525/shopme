package com.dileep.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.admin.currency.ICurrrencyRepository;
import com.dileep.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired
	ICurrrencyRepository currrencyRepository;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
				new Currency("United States Dollar","$","USD"),
				new Currency("British Pound","£","GPB"),
				new Currency("Japanese Yen","¥","JPY"),
				new Currency("Euro","€","EUR"),
				new Currency("Russian Ruble","₱","RUB"),
				new Currency("South Korean Won","₩","KRW"),
				new Currency("Chinese Yuan","¥","CNY"),
				new Currency("Brazilian Real","R$","BRL"),
				new Currency("Australian Dollar","$","AUD"),
				new Currency("Canadian Dollar","$","CND"),
				new Currency("Vietanamese dong","₫","VND"),
				new Currency("Indian Rupee","₹","INR"));
		currrencyRepository.saveAll(listCurrencies);
		Iterable<Currency> iterable = currrencyRepository.findAll();
		assertThat(iterable).size().isEqualTo(12);
	}
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies= currrencyRepository.findAllByOrderByNameAsc();
		
		currencies.forEach(System.out::println);
		
		assertThat(currencies.size()).isGreaterThan(0);
	}
	
}
