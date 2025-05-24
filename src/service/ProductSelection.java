package service;

import model.Product;

public interface ProductSelection {
	void onProductSelected(Product selectedProduct, int quantity);
}
