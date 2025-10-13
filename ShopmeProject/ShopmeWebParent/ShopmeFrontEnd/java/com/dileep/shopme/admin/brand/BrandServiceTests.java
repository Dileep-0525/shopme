package com.dileep.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dileep.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

	@MockBean
	private IBrandRepository brandRepository;
	
	@InjectMocks
	private BrandServiceImpl brandServiceImpl;

	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Long id = null;
		String name = "Acer" ;
		
		Brand brand = new Brand(name);
		
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		
		String result = brandServiceImpl.checkUnique(id, name);
		
		assertThat(result).isEqualTo("Duplicate");
	}
	
	
	@Test
	public void testCheckUniqueInNewModeReturnOk() {
		Long id = null;
		String name = "AMD" ;
		
		Brand brand = new Brand(name);
		
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		
		String result = brandServiceImpl.checkUnique(id, name);
		
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Long id = 1l;
		String name = "Canon" ;
		
		Brand brand = new Brand(id,name);
		
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		
		String result = brandServiceImpl.checkUnique(2l, "Canon");
		
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOk() {
		Long id = 1l;
		String name = "Acer" ;
		
		Brand brand = new Brand(id,name);
		
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		
		String result = brandServiceImpl.checkUnique(2l, "Canon");
		
		assertThat(result).isEqualTo("OK");
	}
	
	
}
