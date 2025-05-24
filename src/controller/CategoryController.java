package controller;



import java.io.IOException;
import java.util.List;

import dao.CategoryDAO;
import model.Category;
import view.CategoryManagement;

public class CategoryController {
	private CategoryDAO categoryDAO;
	private CategoryManagement cm;

	public CategoryController(CategoryDAO categoryDAO, CategoryManagement cm) {
		this.categoryDAO = categoryDAO;
		this.cm = cm;
	}

	public List<Category> getAllCategories() throws ClassNotFoundException, IOException {
		return categoryDAO.getAll();
	}

	public Category getCategoryById(int id) throws ClassNotFoundException, IOException {
		return categoryDAO.get(category -> category.getId() == id);
	}

	public boolean addCategory(Category category) throws ClassNotFoundException, IOException {
		if (!categoryDAO.add(category)) {
			return false;
		}
		cm.updateCategoryTable(getAllCategories());
		return true;
	}

	public boolean updateCategory(Category category) throws ClassNotFoundException, IOException {
		if (!categoryDAO.update(category)) {
			return false;
		}
		cm.updateCategoryTable(getAllCategories());
		return true;
	}

	public boolean deleteCategory(Category category) throws ClassNotFoundException, IOException {
		if (!categoryDAO.delete(category)) {
			return false;
		}
		cm.updateCategoryTable(getAllCategories());
		return true;
	}

	public void searchCategories(String name) throws ClassNotFoundException, IOException {
		List<Category> categories = categoryDAO.searchByName(name);
		cm.updateCategoryTable(categories);
	}
}
