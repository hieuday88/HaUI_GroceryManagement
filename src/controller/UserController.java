package controller;

import java.io.IOException;
import java.util.List;

import dao.UserDAO;
import model.User;

public class UserController {
	private UserDAO userDAO;

	public UserController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public List<User> getAllUsers() throws ClassNotFoundException, IOException {
		return userDAO.getAll();
	}

	public User getUserById(int id) throws ClassNotFoundException, IOException {
		return userDAO.get(category -> category.getId() == id);
	}

	public boolean addUser(User user) throws ClassNotFoundException, IOException {
		if (!userDAO.add(user)) {
			return false;
		}
		return true;
	}

	public boolean updateUser(User user) throws ClassNotFoundException, IOException {
		if (!userDAO.update(user)) {
			return false;
		}
		return true;
	}

	public boolean deleteUser(User user) throws ClassNotFoundException, IOException {
		if (!userDAO.delete(user)) {
			return false;
		}
		return true;
	}
}
