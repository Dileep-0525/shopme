package com.dileep.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dileep.shopme.common.entity.Role;
import com.dileep.shopme.common.entity.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	public static final int USERS_PER_PAGE=4;
	
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> listAll() {
		return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
	}

	@Override
	public List<Role> listRoles() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepository.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	@Override
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		System.out.println(userByEmail);
		if (userByEmail == null)
			return true;
		System.out.println();
		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id)
				return false;
		}
		return true;
	}

	@Override
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
	}

	@Override
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
		userRepository.deleteById(id);
	}

	@Override
	public void updateUserenableStatus(Integer id, boolean enabled) {
		userRepository.updateEnabledStatus(id, enabled);
	}

	@Override
	public Page<User> listByPage(int pageNum , String sortField , String sortDir,String keyword) {
		Sort sort =Sort.by(sortField);
		sort = sortDir.equals("asc")? sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum -1, USERS_PER_PAGE ,sort);
		
		if(keyword!=null) {
			return userRepository.findAll(keyword, pageable);
		}
		
		return userRepository.findAll(pageable);
	}

	@Override
	public User getByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
	
	@Override
	public User updateAccount(User userInForm) {
		User userInDB = userRepository.findById(userInForm.getId()).get();
		if(!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		if(userInForm.getPhotos()!=null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		return userRepository.save(userInDB);
	}
	
	
}
