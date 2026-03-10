package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb139d679b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testSaveAndGetPayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        Payment result = paymentRepository.save(payment);
        Payment found = paymentRepository.getPayment(payment.getId());

        assertSame(payment, result);
        assertSame(payment, found);
    }

    @Test
    void testGetPaymentNotFound() {
        assertNull(paymentRepository.getPayment("missing-id"));
    }

    @Test
    void testGetAllPayments() {
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment(order, "Voucher Code", paymentData1);
        paymentRepository.save(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("address", "Jl. Mawar");
        paymentData2.put("deliveryFee", "15000");
        Payment payment2 = new Payment(order, "Cash On Delivery", paymentData2);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.getAllPayments();

        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payment1));
        assertTrue(allPayments.contains(payment2));
    }
}
