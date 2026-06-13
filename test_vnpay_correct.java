import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class test_vnpay_correct {
    public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replace("+", "%20")
                    .replace("%21", "!")
                    .replace("%28", "(")
                    .replace("%29", ")")
                    .replace("%7E", "~");
        } catch (Exception e) {
            return value;
        }
    }
    
    public static String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            javax.crypto.Mac hmac512 = javax.crypto.Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    public static void main(String[] args) {
        // Test với dữ liệu tương tự như VNPayService
        Map<String, String> params = new LinkedHashMap<>(); // Giữ thứ tự để test
        
        // Thông tin cơ bản
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", "DJJ417XL");
        params.put("vnp_Amount", "10000000"); // 100.000 VND * 100
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_BankCode", "VCB");
        params.put("vnp_TxnRef", "TEST12345");
        params.put("vnp_OrderInfo", "Test payment 100k");
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", "http://localhost:5177/payments/payment-callback");
        params.put("vnp_IpAddr", "127.0.0.1");
        
        // Billing
        params.put("vnp_Bill_Mobile", "0909123456");
        params.put("vnp_Bill_Email", "test@example.com");
        params.put("vnp_Bill_FirstName", "Nguyen");
        params.put("vnp_Bill_LastName", "Van A");
        params.put("vnp_Bill_Address", "123 Nguyen Trai");
        params.put("vnp_Bill_City", "Hanoi");
        params.put("vnp_Bill_Country", "VN");
        params.put("vnp_Bill_State", "HN");
        
        // Invoice
        params.put("vnp_Inv_Phone", "0909123456");
        params.put("vnp_Inv_Email", "test@example.com");
        params.put("vnp_Inv_Customer", "Nguyen Van A");
        params.put("vnp_Inv_Address", "123 Nguyen Trai");
        params.put("vnp_Inv_Company", "ABC Company");
        params.put("vnp_Inv_Taxcode", "0101010101");
        params.put("vnp_Inv_Type", "I");
        
        // Dates
        params.put("vnp_CreateDate", "20260509181000");
        params.put("vnp_ExpireDate", "20260509182500");
        
        // Tạo hash data theo chuẩn VNPay (KHÔNG encode)
        Map<String, String> filteredFields = new HashMap<>();
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(entry.getValue() != null && !entry.getValue().isEmpty() 
                && !entry.getKey().equals("vnp_SecureHash")
                && !entry.getKey().equals("vnp_SecureHashType")){
                filteredFields.put(entry.getKey(), entry.getValue());
            }
        }
        
        List<String> fieldNames = new ArrayList<>(filteredFields.keySet());
        Collections.sort(fieldNames);
        
        StringBuilder hashData = new StringBuilder();
        for(int i = 0; i < fieldNames.size(); i++){
            String fieldName = fieldNames.get(i);
            String fieldValue = filteredFields.get(fieldName);
            if(i > 0){
                hashData.append("&");
            }
            hashData.append(fieldName).append("=").append(fieldValue); // KHÔNG encode
        }
        
        String secretKey = "ERA1IY2056VBV1F4F726NPAEJE2N366Q";
        String hash = hmacSHA512(secretKey, hashData.toString());
        
        // Tạo query string (có encode)
        StringBuilder query = new StringBuilder();
        for(int i = 0; i < fieldNames.size(); i++){
            String fieldName = fieldNames.get(i);
            String fieldValue = filteredFields.get(fieldName);
            if(i > 0){
                query.append("&");
            }
            query.append(encodeValue(fieldName)).append("=").append(encodeValue(fieldValue)); // Có encode
        }
        query.append("&vnp_SecureHash=").append(encodeValue(hash));
        
        System.out.println("=== HASH DATA (KHÔNG encode) ===");
        System.out.println(hashData.toString());
        System.out.println("\n=== SECRET KEY ===");
        System.out.println(secretKey);
        System.out.println("\n=== HASH ===");
        System.out.println(hash);
        System.out.println("\n=== QUERY STRING (có encode) ===");
        System.out.println(query.toString());
        System.out.println("\n=== FULL URL ===");
        System.out.println("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + query.toString());
    }
}
