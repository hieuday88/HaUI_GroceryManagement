package model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String email;
	private String password;
	private boolean isVerify;

	public User() {
	}

	public User(int id, String username, String email, String password, boolean isVerify) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.isVerify = isVerify;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public boolean isVerify() {
		return isVerify;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", isVerify=" + isVerify + "]";
	}

}
