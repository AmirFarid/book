package com.bookstore.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import com.bookstore.dao.UserDAO;
import com.bookstore.entity.Users;;

public class UserServices {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private UserDAO userDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	public UserServices(EntityManager entityManager, HttpServletRequest request, HttpServletResponse response) {
		
		this.request = request;
		this.response = response;
		this.entityManager = entityManager;
		userDAO = new UserDAO(entityManager);
	}
	
	public void listUser() 
			throws ServletException, IOException {
		
		List<Users> listUsers = userDAO.listAll();
	
		
		request.setAttribute("listUsers", listUsers);
		
		String listPage = "user_list.jsp";
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(listPage);
		requestDispatcher.forward(request, response);
		
	}
	
	public void creatUser() throws ServletException , IOException {

		String email = request.getParameter("email");
		String fullName = request.getParameter("fullname");
		String password = request.getParameter("password");
		
		Users user = userDAO.findByEmail(email);
		
		if(user != null) {
			//request.setAttribute("message", "This Email Alredy Existed");
			
			String message = "Could not create user. user with email : " + email + "already exist";
			request.setAttribute("message", message);
			RequestDispatcher dispatcher = request.getRequestDispatcher("message.jsp");
			dispatcher.forward(request, response);
			
			
		}else {
			Users newUser = new Users(email, password, fullName);
			userDAO.create(newUser);
			request.setAttribute("message", "user created successfully");
			listUser();
		}
	}

	public void editUser() throws ServletException , IOException {
		Integer userId = Integer.parseInt(request.getParameter("id"));
		Users user = userDAO.get(userId);
		request.setAttribute("user", user);
		String editUser = "user_form.jsp";
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(editUser);
		requestDispatcher.forward(request , response);
		
	}
	
	public void updateUser() throws ServletException, IOException {

		Integer userId = Integer.parseInt(request.getParameter("userId"));
		Users user = userDAO.get(userId);
		
		String email = request.getParameter("email");
		
		Users existUser = userDAO.findByEmail(email);
		
		/*
		 * System.out.println(","+existUser.getEmail() +","+ email+",");
		 * System.out.println(existUser.getEmail().equals(email));
		 */
		
		if(existUser == null || existUser.getUserId() == userId) {
		user.setEmail(email);
		user.setFullName(request.getParameter("fullname"));
		user.setPassword(request.getParameter("password"));
		 
		userDAO.update(user);
		request.setAttribute("message", "user Updated successfully");
		listUser();
		}else {
			String message = "Could not update user with this email : " + email + "already exist";
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("message.jsp");
			requestDispatcher.forward(request, response);
		}
		
	}

	public void deleteUser() throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("id"));
		userDAO.delete(userId);
		String message = "User deleted successfully";
		request.setAttribute("message", message);
		listUser();
	}
	
	public void login() throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		boolean isUser = userDAO.checkLogin(email, password);
		
		String path;
		
		if(isUser) {
			path = "/admin/";
			request.setAttribute("message", " you log in");
			request.getSession().setAttribute("useremail", email);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
			requestDispatcher.forward(request, response);
		}else{
			path = "login.jsp";
			String message = "Ridi in chera eshtebahe";
			request.setAttribute("message", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
			requestDispatcher.forward(request, response);
		}
		
		
	}
	

}
