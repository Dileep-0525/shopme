package com.dileep.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dileep.shopme.admin.FileUploadUtil;
import com.dileep.shopme.admin.user.UserNotFoundException;
import com.dileep.shopme.admin.user.UserService;
import com.dileep.shopme.admin.user.UserServiceImpl;
import com.dileep.shopme.admin.user.export.UserCSVExporter;
import com.dileep.shopme.admin.user.export.UserExcelExporter;
import com.dileep.shopme.admin.user.export.UserPdfExporter;
import com.dileep.shopme.common.entity.Role;
import com.dileep.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listAll(Model model) {
		return listByPage(1, model, "firstName" , "asc",null);
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum ,Model model ,@Param("sortField") String sortField , @Param("sortDir") String sortDir , @Param("keyword") String keyword ) {

		System.out.println("Sort Field: " +sortField);
		System.out.println("Sort Dirs: " +sortDir);
		Page<User> page = userService.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> listUsers = page.getContent();

		long startCount  = (pageNum-1)  * UserServiceImpl.USERS_PER_PAGE + 1;
		long endCount = startCount + UserServiceImpl.USERS_PER_PAGE - 1;
				if(endCount > page.getTotalElements()) {
					endCount = page.getTotalElements();
				}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		
		return "users/users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {

		List<Role> listRoles = userService.listRoles();

		User user = new User();
		model.addAttribute("user", user);
		user.setEnabled(true);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create new User");
		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		

		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedser = userService.saveUser(user);
			String uploadDir = "user-photos/" +savedser
					.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			userService.saveUser(user);
		}

		redirectAttributes.addFlashAttribute("message", "The User  has been saved Successfully.");
		
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = userService.get(id);
			List<Role> listRoles = userService.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User(Id: " + id + ")");

			return "users/user_form";

		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The User Id " + id + "has been deleted successfully");

		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());

		}
		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnableStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {

		userService.updateUserenableStatus(id, enabled);
		String status = enabled ? "enabled" : "Disabled";
		 
		String message = "The user ID " + id + " has been " + status ;
		
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/users";
	}


	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
		List<User> users = userService.listAll();
		UserCSVExporter exporter = new UserCSVExporter();
		exporter.export(users, httpServletResponse);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
		List<User> users = userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(users, httpServletResponse);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse httpServletResponse) throws IOException {
		List<User> users = userService.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(users, httpServletResponse);
	}

	
	

}


