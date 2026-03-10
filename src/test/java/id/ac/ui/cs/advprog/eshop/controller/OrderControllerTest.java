package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    @Test
    void createOrderPage_returnsView() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderCreate"));
    }

    @Test
    void createOrderPost_redirectsToHistoryAndCallsService() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "Safira"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderService).createOrder(captor.capture());
        Order createdOrder = captor.getValue();
        assertEquals("Safira", createdOrder.getAuthor());
        assertNotNull(createdOrder.getId());
        assertFalse(createdOrder.getProducts().isEmpty());
    }

    @Test
    void orderHistoryPage_returnsDefaultModel() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attribute("author", ""))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void orderHistoryByAuthor_returnsOrders() throws Exception {
        Order order = sampleOrder("order-1");
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderService.findAllByAuthor("Safira")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attribute("author", "Safira"))
                .andExpect(model().attribute("orders", sameInstance(orders)));
    }

    @Test
    void payOrderPage_redirectsWhenOrderNotFound() throws Exception {
        when(orderService.findById("missing-id")).thenReturn(null);

        mockMvc.perform(get("/order/pay/missing-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void payOrderPage_returnsViewWhenOrderFound() throws Exception {
        Order order = sampleOrder("order-1");
        when(orderService.findById("order-1")).thenReturn(order);

        mockMvc.perform(get("/order/pay/order-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPay"))
                .andExpect(model().attribute("order", sameInstance(order)));
    }

    @Test
    void payOrderPost_redirectsWhenOrderNotFound() throws Exception {
        when(orderService.findById("missing-id")).thenReturn(null);

        mockMvc.perform(post("/order/pay/missing-id")
                        .param("method", "Voucher Code"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));

        verify(paymentService, never()).addPayment(any(Order.class), any(String.class), any(Map.class));
        verify(paymentService, never()).setStatus(any(Payment.class), any(String.class));
    }

    @Test
    void payOrderPost_returnsResultWhenOrderFound() throws Exception {
        Order order = sampleOrder("order-1");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("Voucher Code"), any(Map.class))).thenReturn(payment);
        when(paymentService.setStatus(payment, payment.getStatus())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/order-1")
                        .param("method", "Voucher Code")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPayResult"))
                .andExpect(model().attribute("paymentId", payment.getId()))
                .andExpect(model().attribute("paymentStatus", payment.getStatus()))
                .andExpect(model().attribute("order", sameInstance(order)));

        verify(paymentService).addPayment(eq(order), eq("Voucher Code"), any(Map.class));
        verify(paymentService).setStatus(payment, payment.getStatus());
    }

    private Order sampleOrder(String orderId) {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        return new Order(orderId, products, 1708560000L, "Safira");
    }
}
