package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Payment {
    private static final String METHOD_VOUCHER_CODE = "Voucher Code";
    private static final String METHOD_CASH_ON_DELIVERY = "Cash On Delivery";
    private static final String METHOD_BANK_TRANSFER = "Bank Transfer";

    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String REJECTED_STATUS = "REJECTED";

    private final String id;
    private final Order order;
    private final String method;
    private String status;
    private final Map<String, String> paymentData;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = order;
        this.method = method;
        this.paymentData = paymentData == null ? new HashMap<>() : new HashMap<>(paymentData);
        this.status = resolveStatus(method, this.paymentData);
    }

    public void setStatus(String status) {
        if (SUCCESS_STATUS.equals(status) || REJECTED_STATUS.equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String resolveStatus(String method, Map<String, String> paymentData) {
        if (METHOD_VOUCHER_CODE.equals(method)) {
            return isValidVoucherCode(paymentData.get("voucherCode")) ? SUCCESS_STATUS : REJECTED_STATUS;
        }

        if (METHOD_CASH_ON_DELIVERY.equals(method)) {
            return hasText(paymentData.get("address")) && hasText(paymentData.get("deliveryFee"))
                    ? SUCCESS_STATUS : REJECTED_STATUS;
        }

        if (METHOD_BANK_TRANSFER.equals(method)) {
            return hasText(paymentData.get("bankName")) && hasText(paymentData.get("referenceCode"))
                    ? SUCCESS_STATUS : REJECTED_STATUS;
        }

        return REJECTED_STATUS;
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
