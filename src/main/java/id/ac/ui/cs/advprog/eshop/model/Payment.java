package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class Payment {
    private final String id;
    private final Order order;
    private final String method;
    private String status;
    private final Map<String, String> paymentData;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = resolveStatus(method, paymentData);
    }

    public void setStatus(String status) {
        if ("SUCCESS".equals(status) || "REJECTED".equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String resolveStatus(String method, Map<String, String> paymentData) {
        if ("Voucher Code".equals(method)) {
            return isValidVoucherCode(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
        }

        if ("Cash On Delivery".equals(method)) {
            return hasText(paymentData.get("address")) && hasText(paymentData.get("deliveryFee"))
                    ? "SUCCESS" : "REJECTED";
        }

        if ("Bank Transfer".equals(method)) {
            return hasText(paymentData.get("bankName")) && hasText(paymentData.get("referenceCode"))
                    ? "SUCCESS" : "REJECTED";
        }

        return "REJECTED";
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int digitCount = 0;
        for (char current : voucherCode.toCharArray()) {
            if (Character.isDigit(current)) {
                digitCount += 1;
            }
        }
        return digitCount == 8;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
