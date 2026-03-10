package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final String REDIRECT_HISTORY = "redirect:/order/history";
    private static final String PAYMENT_DATA_VOUCHER_CODE = "voucherCode";
    private static final String PAYMENT_DATA_ADDRESS = "address";
    private static final String PAYMENT_DATA_DELIVERY_FEE = "deliveryFee";
    private static final String PAYMENT_DATA_BANK_NAME = "bankName";
    private static final String PAYMENT_DATA_REFERENCE_CODE = "referenceCode";

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "orderCreate";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam("author") String author) {
        Order order = new Order(
                UUID.randomUUID().toString(),
                buildDefaultProducts(),
                System.currentTimeMillis() / 1000,
                author
        );
        orderService.createOrder(order);
        return REDIRECT_HISTORY;
    }

    @GetMapping("/history")
    public String orderHistoryPage(Model model) {
        populateHistoryModel(model, new ArrayList<>(), "");
        return "orderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryByAuthor(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        populateHistoryModel(model, orders, author);
        return "orderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return REDIRECT_HISTORY;
        }

        model.addAttribute("order", order);
        return "orderPay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable String orderId,
                           @RequestParam("method") String method,
                           @RequestParam(value = "voucherCode", required = false) String voucherCode,
                           @RequestParam(value = "address", required = false) String address,
                           @RequestParam(value = "deliveryFee", required = false) String deliveryFee,
                           @RequestParam(value = "bankName", required = false) String bankName,
                           @RequestParam(value = "referenceCode", required = false) String referenceCode,
                           Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return REDIRECT_HISTORY;
        }

        Map<String, String> paymentData = buildPaymentData(
                voucherCode, address, deliveryFee, bankName, referenceCode
        );
        Payment payment = paymentService.addPayment(order, method, paymentData);
        Payment updatedPayment = paymentService.setStatus(payment, payment.getStatus());

        model.addAttribute("paymentId", updatedPayment.getId());
        model.addAttribute("paymentStatus", updatedPayment.getStatus());
        model.addAttribute("order", order);
        return "orderPayResult";
    }

    private List<Product> buildDefaultProducts() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Default Order Product");
        product.setProductQuantity(1);
        products.add(product);
        return products;
    }

    private void populateHistoryModel(Model model, List<Order> orders, String author) {
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
    }

    private Map<String, String> buildPaymentData(String voucherCode,
                                                 String address,
                                                 String deliveryFee,
                                                 String bankName,
                                                 String referenceCode) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(PAYMENT_DATA_VOUCHER_CODE, voucherCode);
        paymentData.put(PAYMENT_DATA_ADDRESS, address);
        paymentData.put(PAYMENT_DATA_DELIVERY_FEE, deliveryFee);
        paymentData.put(PAYMENT_DATA_BANK_NAME, bankName);
        paymentData.put(PAYMENT_DATA_REFERENCE_CODE, referenceCode);
        return paymentData;
    }
}
