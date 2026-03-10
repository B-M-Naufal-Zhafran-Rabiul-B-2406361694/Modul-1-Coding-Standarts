package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private static final String ADMIN_DETAIL_REDIRECT_PREFIX = "redirect:/payment/admin/detail/";
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailFormPage() {
        return "paymentDetailForm";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailPage(@PathVariable String paymentId, Model model) {
        addPaymentAttribute(model, paymentId);
        return "paymentDetail";
    }

    @GetMapping("/admin/list")
    public String paymentAdminListPage(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetailPage(@PathVariable String paymentId, Model model) {
        addPaymentAttribute(model, paymentId);
        return "paymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String paymentAdminSetStatus(@PathVariable String paymentId,
                                        @RequestParam("status") String status) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, status);
        }

        return ADMIN_DETAIL_REDIRECT_PREFIX + paymentId;
    }

    private void addPaymentAttribute(Model model, String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
    }
}
