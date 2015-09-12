package com.durgaindia.findurga;

public class User {
	private String userId;
	private String password;
	private int type;
	private long contactNumber;
	
	public User(String userId, String password, int type, long phoneNumber){
		this.userId  = userId;
		this.password = password;
		this.type = type;
		this.contactNumber = phoneNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(int phoneNumber) {
		this.contactNumber = phoneNumber;
	}
	
	

}
