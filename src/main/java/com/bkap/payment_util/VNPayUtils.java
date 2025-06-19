package com.bkap.payment_util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayUtils {
    public static String createQueryString(Map<String, String> params, String secretKey) {
        List<String> fields = new ArrayList<>(params.keySet());
        Collections.sort(fields);
        StringBuilder query = new StringBuilder();
        for (String field : fields) {
            String value = params.get(field);
            if (value != null && !value.isEmpty()) {
                query.append(URLEncoder.encode(field, StandardCharsets.UTF_8))
                     .append("=")
                     .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                     .append("&");
            }
        }
        if (secretKey != null) {
        	  return query.length() > 0 ? query.substring(0, query.length() - 1) : "";
        }
        return query.length() > 0 ? query.substring(0, query.length() - 1) : "";
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] hmacBytes = sha512Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hmacBytes) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC SHA512", e);
        }
    }
}
