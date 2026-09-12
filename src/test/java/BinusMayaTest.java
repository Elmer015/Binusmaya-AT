import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

public class BinusMayaTest {

    private WebDriver driver;

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void openBinusMaya() {

        driver = new ChromeDriver();

        driver.get("https://binusmaya.binus.ac.id");

        String title = driver.getTitle();

        System.out.println("Page Title: " + title);

        assertTrue(title.length() > 0);
    }

    @Test
    void courseFeatureIsDisplayed() {
        openHomePage();

        String pageText = getPageText();

        assertTrue(pageText.contains("course"),
                "Halaman Binus Maya tidak menampilkan informasi course");
    }

    @Test
    void scheduleFeatureIsDisplayed() {
        openHomePage();

        String pageText = getPageText();

        assertTrue(pageText.contains("schedule"),
                "Halaman Binus Maya tidak menampilkan informasi schedule");
    }

    private void openHomePage() {
        driver = new ChromeDriver();
        driver.get("https://binusmaya.binus.ac.id/home");
    }

    private String getPageText() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(webDriver -> webDriver.findElement(By.tagName("body"))
                .getText().toLowerCase());
    }
}