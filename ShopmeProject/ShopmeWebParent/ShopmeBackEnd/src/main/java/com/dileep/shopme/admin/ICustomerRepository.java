package com.dileep.shopme.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dileep.shopme.common.entity.Customer;

public interface ICustomerRepository extends PagingAndSortingRepository<Customer, Long>{

	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ' , c.firstName ,' ' ,c.lastName, ' ', "
			+ "c.addressLine1,' ',c.addressLine2, ' ',c.city, ' ',c.state, ' ',"
			+ "c.postalCode, ' ',c.country.name) LIKE %?1%")
	public Page<Customer> findAll(String keyword,Pageable pageable);
	
	@Modifying
	@Query("UPDATE Customer c SET c.enabled =?2 WHERE c.id = ?1")
	public void updateEnabledStatus(Long id,boolean enabled);
	
	@Query("SELECT c FROM Customer c WHERE c.email= ?1")
	public Customer findByEmail(String email);
	
	public Long countById(Long id);
	
}
