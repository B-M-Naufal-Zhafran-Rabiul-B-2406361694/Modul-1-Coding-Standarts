package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void paymentDetailFormPage_returnsView() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"));
    }

    @Test
    void paymentDetailPage_returnsPaymentDetailView() throws Exception {
        Payment payment = samplePayment();
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attribute("payment", sameInstance(payment)));
    }

    @Test
    void paymentAdminListPage_returnsAllPayments() throws Exception {
        List<Payment> payments = new ArrayList<>();
        payments.add(samplePayment());
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attribute("payments", sameInstance(payments)));
    }

    @Test
    void paymentAdminDetailPage_returnsDetailView() throws Exception {
        Payment payment = samplePayment();
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attribute("payment", sameInstance(payment)));
    }

    @Test
    void paymentAdminSetStatus_callsServiceWhenPaymentFound() throws Exception {
        Payment payment = samplePayment();
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/" + payment.getId())
                        .param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/" + payment.getId()));

        verify(paymentService).setStatus(payment, "REJECTED");
    }

    @Test
    void paymentAdminSetStatus_skipsServiceWhenPaymentNotFound() throws Exception {
        when(paymentService.getPayment("missing-id")).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/missing-id")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/missing-id"));

        verify(paymentService, never()).setStatus(org.mockito.ArgumentMatchers.any(Payment.class),
                org.mockito.ArgumentMatchers.anyString());
    }

    private Payment samplePayment() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("order-1", products, 1708560000L, "Safira");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        return new Payment(order, "Voucher Code", paymentData);
    }
}
