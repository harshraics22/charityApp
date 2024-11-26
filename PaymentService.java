package in.ashokit.service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.ashokit.controller.SendEmailService;
import in.ashokit.dto.StudentOrder;
import in.ashokit.repo.StudentOrderRepo;

@Service
public class PaymentService {

    @Autowired
    private StudentOrderRepo orderRepo;

    @Autowired
    private SendEmailService sendEmailService; // Autowired Email service

    private RazorpayClient client;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private static final String PAYMENT_SUCCESS = "CONFIRMED";
    private static final String PAYMENT_FAILED = "FAILED";

    /**
     * Creates an order on Razorpay and stores it in the database.
     */
    public StudentOrder createOrder(StudentOrder studentOrder) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", studentOrder.getAmount() * 100); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", studentOrder.getEmail());

        this.client = new RazorpayClient(keyId, keySecret);
        Order razorPayOrder = client.Orders.create(orderRequest);

        studentOrder.setRazorPayOrderId(razorPayOrder.get("id"));
        studentOrder.setOrderStatus(razorPayOrder.get("status"));

        orderRepo.save(studentOrder);

        return studentOrder;
    }

    /**
     * Verifies payment and updates the order status.
     */
    public StudentOrder verifyPaymentAndUpdateOrderStatus(Map<String, String> respPayload) {
        StudentOrder studentOrder = null;

        try {
            String razorpayOrderId = respPayload.get("razorpay_order_id");
            String razorpayPaymentId = respPayload.get("razorpay_payment_id");
            String razorpaySignature = respPayload.get("razorpay_signature");

            // Verify the signature to ensure the payload is genuine
            boolean isValidSignature = verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

            if (isValidSignature) {
                studentOrder = orderRepo.findByRazorPayOrderId(razorpayOrderId);
                if (studentOrder != null) {
                    studentOrder.setOrderStatus(PAYMENT_SUCCESS);
                    orderRepo.save(studentOrder);

                    // Send the confirmation email after successful payment
                    sendEmail(studentOrder);
                }
            } else {
                System.out.println("Invalid signature for order ID: " + razorpayOrderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentOrder;
    }

    /**
     * Verifies the Razorpay signature.
     */
    private boolean verifySignature(String orderId, String paymentId, String signature) throws RazorpayException {
        String generatedSignature = HmacSHA256(orderId + "|" + paymentId, keySecret);
        return generatedSignature.equals(signature);
    }

    /**
     * Generates HmacSHA256 signature.
     */
    private String HmacSHA256(String data, String key) throws RazorpayException {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(),
                    "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            return new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));
        } catch (Exception e) {
            throw new RazorpayException("Failed to calculate signature.", e);
        }
    }

    /**
     * Sends a payment confirmation email.
     */
    
    private void sendEmail(StudentOrder studentOrder) {
        String emailSubject = "Payment Successful";
        String emailBody = String.format(
                "Dear %s,\n\n" +
                "We are pleased to inform you that your payment of ₹%s has been successfully processed.\n" +
                "your phone no is %s\n"		+
                "Here are your order details:\n" +
                "----------------------------------\n" +
                "Payment ID: %s\n" +
                "Amount Paid: ₹%s\n" +
                "----------------------------------\n" +
                "Thank you for choosing Harsh Rai. If you have any questions, feel free to contact our support team.\n\n" +
                "Best regards,\n" +
                "The Harsh Team\n" +
                "support@Harsh.com | +91-7991404959\n" +
                "www.harshrai.com",
                studentOrder.getName(),
                studentOrder.getAmount(),
                studentOrder.getPhno(),
                studentOrder.getRazorPayOrderId(),
                studentOrder.getAmount()
        );

        
        sendEmailService.sendEmail(studentOrder.getEmail(), emailSubject, emailBody);
    }
}
