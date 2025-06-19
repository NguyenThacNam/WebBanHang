package com.bkap.dto;

import java.util.List;

public class OrderFormDTO {
    private List<CartItemDTO> cartItems;
    private String address;
    private String note;
    private String paymentMethod;
    // getter + setter
    
    public OrderFormDTO() {}

	public  List<CartItemDTO> getCartItems() {
		return cartItems;
	}

	public  void setCartItems(List<CartItemDTO> cartItems) {
		this.cartItems = cartItems;
	}

	public  String getAddress() {
		return address;
	}

	public  void setAddress(String address) {
		this.address = address;
	}

	public  String getNote() {
		return note;
	}

	public  void setNote(String note) {
		this.note = note;
	}

	public  String getPaymentMethod() {
		return paymentMethod;
	}

	public  void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
    
}
