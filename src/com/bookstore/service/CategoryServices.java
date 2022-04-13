package com.bookstore.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.dao.CategoryDAO;
import com.bookstore.entity.Category;

public class CategoryServices {
	private EntityManager entityManager;
	private CategoryDAO categoryDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	public CategoryServices(EntityManager entityManager,HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.entityManager = entityManager;
		categoryDAO = new CategoryDAO(entityManager);
	}
	
	
	public void listCategory() throws ServletException, IOException {
		List<Category> listAllCategories = categoryDAO.listAll();
		
		request.setAttribute("listCategory", listAllCategories);
		
		String listPage = "category_list.jsp";
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(listPage);
		requestDispatcher.forward(request, response);
		
	}
	
	public void createCategory() throws ServletException , IOException {
		String categoryName = request.getParameter("name");
		String message;
		Category existCategory = categoryDAO.findByName(categoryName);
		if(existCategory == null) {
			Category newCategory = new Category(categoryName);
			categoryDAO.create(newCategory);
			message = "This Category Created Successfully";
		}else {
			message = "category cration faild";
		}
		request.setAttribute("message", message);
		listCategory();
	}


	public void deleteCategory() throws ServletException, IOException {
		
		int categoryId = Integer.parseInt(request.getParameter("id"));
		categoryDAO.delete(categoryId);
		
		String message = "Category" + categoryId + "has been removed";
		request.setAttribute("message", message);
		
		listCategory();
		
	}
}
