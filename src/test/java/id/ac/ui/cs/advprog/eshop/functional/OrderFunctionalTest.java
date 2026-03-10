package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {
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
    void createOrderAndFindInHistory(ChromeDriver driver) {
        String authorName = "Safira-" + System.currentTimeMillis();

        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys(authorName);
        driver.findElement(By.id("createOrderButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/order/history"));

        driver.findElement(By.id("authorSearchInput")).clear();
        driver.findElement(By.id("authorSearchInput")).sendKeys(authorName);
        driver.findElement(By.id("searchOrderButton")).click();

        wait.until(webDriver -> webDriver.getPageSource().contains(authorName));
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        boolean found = rows.stream().anyMatch(row -> row.getText().contains(authorName));
        assertTrue(found, "Expected created order to appear in order history.");
    }
}
