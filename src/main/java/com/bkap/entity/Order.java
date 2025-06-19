package com.bkap.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với user
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private String address;

    private String note;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();
    
    
    @Column(name = "payment_status")
    private String paymentStatus; // PENDING, PAID, FAILED, CANCELLED

    @Column(name = "transaction_id")
    private String transactionId; // Lưu ID giao dịch từ cổng thanh toán

    private String status;
    
    public Order() {
    }
    
    // Liên kết với OrderDetail
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

	

	public Order(Long id, User user, String address, String note, String paymentMethod, LocalDateTime orderDate,
			String paymentStatus, String transactionId, String status, List<OrderDetail> orderDetails) {
		super();
		this.id = id;
		this.user = user;
		this.address = address;
		this.note = note;
		this.paymentMethod = paymentMethod;
		this.orderDate = orderDate;
		this.paymentStatus = paymentStatus;
		this.transactionId = transactionId;
		this.status = status;
		this.orderDetails = orderDetails;
	}

	public  Long getId() {
		return id;
	}

	public  void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public  LocalDateTime getOrderDate() {
		return orderDate;
	}

	public  void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public  String getStatus() {
		return status;
	}

	public  void setStatus(String status) {
		this.status = status;
	}

	public  List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    
}
