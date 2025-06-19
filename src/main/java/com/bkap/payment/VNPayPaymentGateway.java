package com.bkap.payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bkap.entity.Order;
import com.bkap.gateway.PaymentGateway;
import com.bkap.payment_util.VNPayUtils;

@Component
public class VNPayPaymentGateway implements PaymentGateway {
    private static final String VNPAY_TMN_CODE = "3NKHWBM1"; // Mã merchant từ VNPay
    private static final String VNPAY_HASH_SECRET = "Y058GVX1B152X10IES2I0IJHE42YOC79"; // Khóa bí mật từ VNPay
    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"; // URL sandbox
    private static final String VNPAY_RETURNURL = "http://localhost:8080/api/order/payment-callback";
   

    @Override
    public String createPaymentRequest(Order order, String ipAddress) {
        // Tính tổng tiền từ OrderDetails
        long amount = order.getOrderDetails().stream()
                .mapToLong(detail -> (long) (detail.getPrice() * detail.getQuantity() * 100)) // VNPay yêu cầu amount * 100
                .sum();

        // Tạo tham số cho VNPay
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", VNPAY_TMN_CODE);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", order.getId().toString()); // Dùng orderId làm mã giao dịch
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng " + order.getId());
        vnpParams.put("vnp_OrderType", "250000"); // Mã loại hàng hóa
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VNPAY_RETURNURL); // URL callback
        vnpParams.put("vnp_IpAddr", ipAddress);
        vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Tạo chữ ký (hash)
        String queryString = VNPayUtils.createQueryString(vnpParams, VNPAY_HASH_SECRET);
        vnpParams.put("vnp_SecureHash", VNPayUtils.hmacSHA512(VNPAY_HASH_SECRET, queryString));

        // Tạo URL thanh toán
        String paymentUrl = VNPAY_URL + "?" + VNPayUtils.createQueryString(vnpParams, null);
        return paymentUrl;
    }

    @Override
    public boolean verifyPaymentCallback(Map<String, String> callbackParams) {
        String vnpSecureHash = callbackParams.get("vnp_SecureHash");
        callbackParams.remove("vnp_SecureHash");

        String signData = VNPayUtils.createQueryString(callbackParams, null);
        String computedHash = VNPayUtils.hmacSHA512(VNPAY_HASH_SECRET, signData);

        return vnpSecureHash.equals(computedHash) && "00".equals(callbackParams.get("vnp_ResponseCode"));
    }
}