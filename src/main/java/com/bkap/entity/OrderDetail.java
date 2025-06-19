package com.bkap.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDER_DETAILS")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với Order
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    // Liên kết với Product
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int quantity;

    private Double price;

    
    
    public OrderDetail() {
    }
    
	public OrderDetail(Long id, Order order, Product product, int quantity, Double price) {
		super();
		this.id = id;
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}

	public  Long getId() {
		return id;
	}

	public  void setId(Long id) {
		this.id = id;
	}

	public  Order getOrder() {
		return order;
	}

	public  void setOrder(Order order) {
		this.order = order;
	}

	public  Product getProduct() {
		return product;
	}

	public  void setProduct(Product product) {
		this.product = product;
	}

	public  int getQuantity() {
		return quantity;
	}

	public  void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public  Double getPrice() {
		return price;
	}

	public  void setPrice(Double double1) {
		this.price = double1;
	}

    // Getters/setters
    
    
}
