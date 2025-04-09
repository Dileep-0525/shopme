package com.dileep.shopme.admin.user;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dileep.shopme.common.entity.Role;
import com.dileep.shopme.common.entity.User;

public interface UserService {

	public List<User> listAll();
	
	public List<Role> listRoles();
	
	public User saveUser(User user);
	
	public boolean isEmailUnique(Integer id ,String email);

	public User get(Integer id) throws UserNotFoundException;
	
	public void delete(Integer id)throws UserNotFoundException;
	
	public void updateUserenableStatus(Integer id , boolean enabled);

	public Page<User> listByPage(int pageNum , String sortField , String sortDir,String keyword);
}
