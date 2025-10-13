package com.dileep.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.dileep.shopme.common.entity.Role;
import com.dileep.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("dileep@gmail.com","Dileep@123","Dileep","V");
		user.addRole(roleAdmin);

		User savedUser = userRepository.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {

		User user = new User("sanju@gmail.com","sanju@123","Sanju","Raj");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		user.addRole(roleAssistant);
		user.addRole(roleEditor);

		User saveduser = userRepository.save(user);
		assertThat(saveduser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUser = userRepository.findAll();
		listUser.forEach(user -> System.out.println(user));

	}

	@Test
	public void testGetUserbyId() {

		User user = userRepository.findById(1).get();
		System.out.println(user);	
		assertThat(user).isNotNull();
	}

	@Test
	public void testUpadateUserDetails() {
		User user = userRepository.findById(1).get();
		user.setEnabled(true);
		user.setEmail("dileep525@gmail.com");

		userRepository.save(user);
	}

	@Test
	public void testUpdateUserRoles() {
		User user = userRepository.findById(3).get();
		Role roleEditor = new Role(2);
		Role roleSalesperson = new Role(1);

		user.getRoles().remove(roleEditor);
		user.addRole(roleSalesperson);

		userRepository.save(user);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 5;
		userRepository.deleteById(userId);
		
	}
	@Test
	public void testGetUserByEmail() {
		String email = "dileep525@gmail.com";
		User user=userRepository.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = userRepository.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		userRepository.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 1;
		userRepository.updateEnabledStatus(id, true);
	}
	
	@Test 
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize=4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User>	page = userRepository.findAll(pageable);
		List<User> listUsers= page.getContent();
		listUsers.forEach(user ->System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUsers() {
		String keyword = "Dileep";
		
		int pageNumber = 0;
		int pageSize=4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User>	page = userRepository.findAll(keyword, pageable);
		List<User> listUsers= page.getContent();
		listUsers.forEach(user ->System.out.println(user));
		
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	

}


