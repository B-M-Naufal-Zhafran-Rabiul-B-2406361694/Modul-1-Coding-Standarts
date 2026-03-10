package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void payOrderAndOpenPaymentDetail(ChromeDriver driver) {
        String paymentId = createPaymentByVoucher(driver);

        driver.get(baseUrl + "/payment/detail/" + paymentId);
        String shownPaymentId = driver.findElement(By.id("paymentIdValue")).getText();
        String shownMethod = driver.findElement(By.id("paymentMethodValue")).getText();

        assertEquals(paymentId, shownPaymentId);
        assertEquals("Voucher Code", shownMethod);
    }

    @Test
    void adminCanSetPaymentStatusToRejected(ChromeDriver driver) {
        String paymentId = createPaymentByVoucher(driver);

        driver.get(baseUrl + "/payment/admin/detail/" + paymentId);
        driver.findElement(By.id("rejectButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/payment/admin/detail/" + paymentId));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("paymentStatusValue"), "REJECTED"));

        String status = driver.findElement(By.id("paymentStatusValue")).getText();
        assertEquals("REJECTED", status);
    }

    private String createPaymentByVoucher(ChromeDriver driver) {
        String authorName = "Author-" + System.currentTimeMillis();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys(authorName);
        driver.findElement(By.id("createOrderButton")).click();

        wait.until(ExpectedConditions.urlContains("/order/history"));
        driver.findElement(By.id("authorSearchInput")).clear();
        driver.findElement(By.id("authorSearchInput")).sendKeys(authorName);
        driver.findElement(By.id("searchOrderButton")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody tr a.btn-success")));
        driver.findElement(By.cssSelector("tbody tr a.btn-success")).click();

        wait.until(ExpectedConditions.urlContains("/order/pay/"));
        assertTrue(driver.getCurrentUrl().contains("/order/pay/"));

        driver.findElement(By.id("methodSelect")).sendKeys("Voucher Code");
        driver.findElement(By.id("voucherCodeInput")).sendKeys("ESHOP1234ABC5678");
        driver.findElement(By.id("payOrderButton")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("paymentIdValue")));
        return driver.findElement(By.id("paymentIdValue")).getText();
    }
}
