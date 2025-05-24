package dao;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Product;
import util.FileConnector;

public class ProductDAO implements DAO<Product> {
	private final String FILE_PATH = "/src/db/products.bin";
	private final FileConnector<Product> fileConnector = new FileConnector<Product>();

	@Override
	public Product get(Predicate<Product> predicate) throws ClassNotFoundException, IOException {
		List<Product> products = fileConnector.readFromFile(FILE_PATH);
		for (Product product : products) {
			if (predicate.test(product)) {
				return product;
			}
		}
		return null;
	}

	@Override
	public List<Product> getAll() throws ClassNotFoundException, IOException {
		return fileConnector.readFromFile(FILE_PATH);
	}

	@Override
	public boolean add(Product product) throws ClassNotFoundException, IOException {
		List<Product> products = getAll();
		for (Product p : products) {
			if (p.getId() == product.getId()) {
				return false;
			}
		}
		fileConnector.appendObject(FILE_PATH, product);
		return true;
	}

	@Override
	public boolean update(Product updatedProduct) throws ClassNotFoundException, IOException {
		List<Product> products = fileConnector.readFromFile(FILE_PATH);
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getId() == updatedProduct.getId()) {
				products.set(i, updatedProduct);
				break;
			}
		}
		fileConnector.writeToFile(FILE_PATH, products);
		return true;
	}

	@Override
	public boolean delete(Product deletedProduct) throws ClassNotFoundException, IOException {
		List<Product> products = fileConnector.readFromFile(FILE_PATH);
		products.removeIf(product -> product.getId() == deletedProduct.getId());
		fileConnector.writeToFile(FILE_PATH, products);
		return true;
	}

	public List<Product> searchByName(String name) throws ClassNotFoundException, IOException {
		List<Product> products = getAll();
		return products.stream().filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

}
