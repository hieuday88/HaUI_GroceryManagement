package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Bill implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int adminId;
	private Date date;
	private HashMap<Product, Integer> products;
	private double total;

	public Bill() {
	}

	public Bill(String id, String name, int adminId, Date date, HashMap<Product, Integer> products) {
		this.id = id;
		this.name = name;
		this.adminId = adminId;
		this.date = date;
		this.products = products;
		this.total = 0;
		calcTotal();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAdminId() {
		return adminId;
	}

	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
		return sdf.format(date);
	}

	public HashMap<Product, Integer> getProducts() {
		return products;
	}

	public double getTotal() {
		return total;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setProducts(HashMap<Product, Integer> products) {
		this.products = products;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void calcTotal() {
		for (Product product : products.keySet()) {
			total += product.getPrice() * products.get(product);
		}
	}
}
