package com.dileep.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dileep.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	@MockBean
	private ICategoryRepository repository;
	
	@InjectMocks
	private CategoryServiceImpl service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		
		Long id = null;
		String name = "Computers";
		String alias = "abc";
		Category category = new Category(null, name, alias);
		Mockito.when(repository.findByName(name)).thenReturn(category);
		Mockito.when(repository.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlice() {
		
		Long id = null;
		String name = "NameABC";
		String alias = "computers";
		Category category = new Category(id, name, alias);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		Mockito.when(repository.findByAlias(alias)).thenReturn(category);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		
		Long id = null;
		String name = "Computers";
		String alias = "abc";
		Category category = new Category(null, name, alias);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		Mockito.when(repository.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		
		Long id = 1l;
		String name = "Computers";
		String alias = "abc";
		Category category = new Category(2l, name, alias);
		Mockito.when(repository.findByName(name)).thenReturn(category);
		Mockito.when(repository.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateName");
	} 
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlice() {
		
		Long id = 1l;
		String name = "NameABC";
		String alias = "computers";
		Category category = new Category(2l, name, alias);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		Mockito.when(repository.findByAlias(alias)).thenReturn(category);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		
			
		Long id = 1l;
		String name = "Computers";
		String alias = "abc";
		Category category = new Category(1l, name, alias);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		Mockito.when(repository.findByAlias(alias)).thenReturn(null);
		
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");
	}
	
}
