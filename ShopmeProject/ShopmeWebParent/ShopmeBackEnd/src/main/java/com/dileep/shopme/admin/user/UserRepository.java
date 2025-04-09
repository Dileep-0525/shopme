package com.dileep.shopme.admin.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dileep.shopme.common.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

	@Query("SELECT u From User u WHERE u.email=:email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);

	@Query("SELECT u from User u WHERE u.firstName LIKE %?1%OR u.lastName LIKE %?1%OR u.email LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Modifying
	@Query("UPDATE  User u SET u.enabled =?2 WHERE u.id= ?1")
	public void updateEnabledStatus(Integer id , boolean enabled);

	
}
