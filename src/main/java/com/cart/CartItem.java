package com.cart;

public class CartItem {
	private Long productId;
	private String productName;
	private String image;
	private int quantity;
	private double price;

	public CartItem(Long productId, String productName, String image, int quantity, double price) {
		this.productId = productId;
		this.productName = productName;
		this.image = image;
		this.quantity = quantity;
		this.price = price;
	}

	public double getTotalPrice() {
		return this.quantity * this.price;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
