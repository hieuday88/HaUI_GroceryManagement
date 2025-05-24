package dao;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Category;
import util.FileConnector;

public class CategoryDAO implements DAO<Category> {
	private final String FILE_PATH = "/src/db/categories.bin";
	private final FileConnector<Category> fileConnector = new FileConnector<Category>();

	@Override
	public Category get(Predicate<Category> predicate) throws ClassNotFoundException, IOException {
		List<Category> categories = getAll();
		for (Category category : categories) {
			if (predicate.test(category)) {
				return category;
			}
		}
		return null;
	}

	@Override
	public List<Category> getAll() throws ClassNotFoundException, IOException {
		return fileConnector.readFromFile(FILE_PATH);
	}

	@Override
	public boolean add(Category category) throws ClassNotFoundException, IOException {
		List<Category> categories = getAll();
		for (Category c : categories) {
			if (c.getId() == category.getId()) {
				return false;
			}
		}
		fileConnector.appendObject(FILE_PATH, category);
		return true;
	}

	@Override
	public boolean update(Category updatedCategory) throws ClassNotFoundException, IOException {
		List<Category> categories = fileConnector.readFromFile(FILE_PATH);
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getId() == updatedCategory.getId()) {
				categories.set(i, updatedCategory);
				break;
			}
		}
		fileConnector.writeToFile(FILE_PATH, categories);
		return true;
	}

	@Override
	public boolean delete(Category deletedCategory) throws ClassNotFoundException, IOException {
		List<Category> categories = fileConnector.readFromFile(FILE_PATH);
		categories.removeIf(category -> category.getId() == deletedCategory.getId());
		fileConnector.writeToFile(FILE_PATH, categories);
		return true;
	}

	public List<Category> searchByName(String name) throws ClassNotFoundException, IOException {
		List<Category> categories = getAll();
		return categories.stream().filter(category -> category.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

}
