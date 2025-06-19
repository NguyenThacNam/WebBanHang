package com.bkap.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bkap.entity.Order;
import com.bkap.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public String listOrders(Model model) {
		List<Order> orders = orderService.getAllOrders();
		model.addAttribute("orders", orders);
		return "admin/order/order-list"; 
	}

	@PostMapping("/delete")
	public String deleteOrder(@RequestParam("id") Long id, RedirectAttributes redirect) {
		try {
			orderService.deleteOrder(id);
			redirect.addFlashAttribute("success", "Đã xóa đơn hàng #" + id);
		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Xóa thất bại: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
	    Order order = orderService.getOrderById(id);
	    model.addAttribute("order", order);
	    return "admin/order/order-edit";
	}

	@PostMapping("/update-status")
	public String updateOrderStatus(@RequestParam("id") Long orderId, @RequestParam("status") String status,
			RedirectAttributes redirect) {
		try {
			orderService.updateOrderStatus(orderId, status);
			redirect.addFlashAttribute("success", "Cập nhật trạng thái đơn #" + orderId + " thành công");
		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}

	@GetMapping("/view/{id}")
	public String viewOrder(@PathVariable Long id, Model model) {
	    Order order = orderService.getOrderById(id);
	    if (order == null) {
	        return "redirect:/admin/orders?error=notfound";
	    }

	    // Tính tổng tiền
	    BigDecimal totalAmount = orderService.calculateTotalAmount(order);

	    // Đưa dữ liệu ra view
	    model.addAttribute("order", order);
	    model.addAttribute("totalAmount", totalAmount);

	    return "admin/order/order-detail"; // Trang xem chi tiết đơn hàng
	}

}
