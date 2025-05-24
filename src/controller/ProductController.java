package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dao.ProductDAO;
import model.Product;
import view.ProductManagement;

public class ProductController {
	private ProductDAO productDAO;
	private ProductManagement pm;

	public ProductController(ProductDAO productDAO, ProductManagement pm) {
		this.productDAO = productDAO;
		this.pm = pm;
	}

	public List<Product> getAllProducts() throws ClassNotFoundException, IOException {
		return productDAO.getAll();
	}

	public Product getProductById(int id) throws ClassNotFoundException, IOException {
		return productDAO.get(Product -> Product.getId() == id);
	}

	public boolean addProduct(Product Product) throws ClassNotFoundException, IOException {
		if (!productDAO.add(Product)) {
			return false;
		}
		if (pm != null)
			pm.updateProductTable(getAllProducts());
		return true;
	}

	public boolean updateProduct(Product Product) throws ClassNotFoundException, IOException {
		if (!productDAO.update(Product)) {
			return false;
		}
		if (pm != null)
			pm.updateProductTable(getAllProducts());
		return true;
	}

	public boolean deleteProduct(Product Product) throws ClassNotFoundException, IOException {
		if (!productDAO.delete(Product)) {
			return false;
		}
		if (pm != null)
			pm.updateProductTable(getAllProducts());
		return true;
	}

	public void searchProducts(String name) throws ClassNotFoundException, IOException {
		List<Product> products = productDAO.searchByName(name);
		pm.updateProductTable(products);
	}

	public void sortProductsByPrice(List<Product> products, boolean isINC) {
		Collections.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product p1, Product p2) {
				if (isINC) {
					return Double.compare(p1.getPrice(), p2.getPrice());
				} else {
					return Double.compare(p2.getPrice(), p1.getPrice());
				}
			}
		});
		pm.updateProductTable(products);
	}
	public void sortProductsById(List<Product> products, boolean isINC) {
    Collections.sort(products, new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            if (isINC) {
                return Integer.compare(p1.getId(), p2.getId());
            } else {
                return Integer.compare(p2.getId(), p1.getId());
            }
        }
    });
    pm.updateProductTable(products);
}
}
