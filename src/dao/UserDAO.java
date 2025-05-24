package dao;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import model.User;
import util.FileConnector;
import util.HashPassword;

public class UserDAO implements DAO<User> {
	private final String FILE_PATH = "/src/db/users.bin";
	private final FileConnector<User> fileConnector = new FileConnector<User>();

	public boolean isUserExist(String username, String email) throws ClassNotFoundException, IOException {
		List<User> users = fileConnector.readFromFile(FILE_PATH);
		return users.stream().anyMatch(user -> user.getUsername().equals(username) || user.getEmail().equals(email));
	}

	@Override
	public User get(Predicate<User> predicate) throws ClassNotFoundException, IOException {
		List<User> users = fileConnector.readFromFile(FILE_PATH);
		for (User user : users) {
			if (predicate.test(user)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public List<User> getAll() throws ClassNotFoundException, IOException {
		return fileConnector.readFromFile(FILE_PATH);
	}

	@Override
	public boolean add(User user) throws ClassNotFoundException, IOException {
		if (isUserExist(user.getUsername(), user.getEmail())) {
			return false;
		}
		user.setPassword(HashPassword.hashPassword(user.getPassword()));
		fileConnector.appendObject(FILE_PATH, user);
		return true;

	}

	@Override
	public boolean update(User updatedUser) throws ClassNotFoundException, IOException {
		List<User> users = fileConnector.readFromFile(FILE_PATH);
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getEmail().equals(updatedUser.getEmail())) {
				users.set(i, updatedUser);
				break;
			}
		}
		fileConnector.writeToFile(FILE_PATH, users);
		return true;
	}

	@Override
	public boolean delete(User deletedUser) throws ClassNotFoundException, IOException {
		List<User> users = fileConnector.readFromFile(FILE_PATH);
		users.removeIf(user -> user.equals(deletedUser));
		fileConnector.writeToFile(FILE_PATH, users);
		return true;
	}
}
