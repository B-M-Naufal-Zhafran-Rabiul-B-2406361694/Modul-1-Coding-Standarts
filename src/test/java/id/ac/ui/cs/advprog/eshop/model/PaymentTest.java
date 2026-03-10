package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest {
    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        this.order = new Order("13652556-012a-4c07-b546-54eb139d679b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreateVoucherPaymentSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(this.order, "Voucher Code", paymentData);

        assertNotNull(payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
    }

    @Test
    void testCreateVoucherPaymentInvalidCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALID");

        Payment payment = new Payment(this.order, "Voucher Code", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateCashOnDeliveryPaymentInvalidData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "15000");

        Payment payment = new Payment(this.order, "Cash On Delivery", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentInvalidData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF-001");

        Payment payment = new Payment(this.order, "Bank Transfer", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }
}
