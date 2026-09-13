import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class BinusMayaTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BINUSMAYA_URL = "https://binusmaya.binus.ac.id/home";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        // EAGER mode: allows interacting immediately once DOM is ready without waiting for heavy external assets
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-search-engine-choice-screen");

        int port = 9222;

        if (isDebuggerResponding(port)) {
            System.out.println("[INFO] Attaching directly to your open Chrome on port " + port + "...");
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:" + port);
        } else {
            System.out.println("[INFO] Launching fast Chrome automation session...");
            String profileDir = System.getProperty("user.home") + File.separator + ".chrome-binus-profile";
            new File(profileDir).mkdirs();
            options.addArguments("--user-data-dir=" + profileDir);
            options.addArguments("--profile-directory=Default");
        }

        System.out.println("[INFO] Initializing WebDriver...");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    private boolean isDebuggerResponding(int port) {
        try {
            URL url = new URL("http://127.0.0.1:" + port + "/json/version");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(600);
            conn.setReadTimeout(600);
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            System.out.println("[INFO] Closing browser session in 3 seconds...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            driver.quit();
        }
    }

    /**
     * Test 1: Open LMS and Click Schedule button in sidebar (Picture 1).
     */
    @Test
    void testOpenSchedule() {
        authenticateAndOpenLms();

        System.out.println("[INFO] Finding and clicking Schedule sidebar link...");
        WebElement scheduleLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@class='nav-link' and contains(@href, '/lms/schedule')] | //a[contains(@href, '/lms/schedule')] | //li[contains(@class,'nav-item')]//a[contains(., 'Schedule')]")));

        clickElement(scheduleLink);

        System.out.println("[INFO] Verifying Schedule page...");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("schedule"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(translate(text(), 'SCHEDULE', 'schedule'), 'schedule')]"))
        ));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("[SUCCESS] Schedule page loaded! Current URL: " + currentUrl);
        assertTrue(currentUrl.toLowerCase().contains("schedule"),
                "URL does not contain 'schedule' after clicking Schedule section");
    }

    /**
     * Test 2: Open Courses from sidebar and Click 'Automation Testing' course (Picture 2).
     */
    @Test
    void testOpenCoursesAndSelectAutomationTesting() {
        authenticateAndOpenLms();

        System.out.println("[INFO] Finding and clicking Courses sidebar link...");
        WebElement coursesLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@class='nav-link' and contains(@href, '/lms/course')] | //a[contains(@href, '/lms/course')] | //li[contains(@class,'nav-item')]//a[contains(., 'Courses')]")));

        clickElement(coursesLink);

        System.out.println("[INFO] Finding and clicking 'Automation Testing' course link...");
        WebElement autoTestingCourse = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/lms/course/') and contains(normalize-space(), 'Automation Testing')] | //h3[contains(@class, 'item') and contains(@class, 'title')]//a[contains(., 'Automation Testing')] | //div[contains(@class, 'C--CardClass')][.//a[contains(., 'Automation Testing')]]//a[contains(., 'Automation Testing')] | //*[contains(text(), 'Automation Testing')]")));

        clickElement(autoTestingCourse);

        System.out.println("[INFO] Verifying Automation Testing page...");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("session"),
                ExpectedConditions.urlContains("course"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Automation Testing') or contains(text(), 'COMP6883001')]"))
        ));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("[SUCCESS] Automation Testing course opened! Current URL: " + currentUrl);
        assertTrue(currentUrl.toLowerCase().contains("course") || currentUrl.toLowerCase().contains("session"),
                "Current URL should indicate course session navigation");
    }

    /**
     * Test 3: Open Latest Forum Post from the Dashboard widget (Picture 3).
     */
    @Test
    void testOpenLatestForumInDashboard() {
        authenticateAndOpenLms();

        System.out.println("[INFO] Finding and clicking latest forum post...");
        WebElement latestPost = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Latest Forum Posts')]/following::div[contains(., 'Replied on')][1] | //*[contains(text(), 'Latest Forum Posts')]/ancestor::div[contains(@class, 'card') or contains(@class, 'col') or contains(@class, 'box') or contains(@class, 'widget') or contains(@class, 'panel')]//div[contains(., 'Replied on') or contains(., 'Lecturer') or contains(., 'Anemic Code')][last()] | //*[contains(text(), 'Anemic Code')] | //*[contains(text(), 'Latest Forum Posts')]/following::div[contains(@class, 'thread-author-name')][1]")));

        clickElement(latestPost);

        System.out.println("[INFO] Verifying forum thread page...");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("forum"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(translate(text(), 'FORUM', 'forum'), 'forum')] | //*[contains(text(), 'Thread')] | //*[contains(text(), 'Post')] | //*[contains(text(), 'Anemic Code')]"))
        ));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("[SUCCESS] Forum thread opened! Current URL: " + currentUrl);
        assertTrue(currentUrl.toLowerCase().contains("forum") || currentUrl.toLowerCase().contains("lms"),
                "URL should contain forum or lms path after clicking latest forum post");
    }

    /**
     * Open Binusmaya portal, click LMS icon as soon as found, and switch to LMS.
     */
    private void authenticateAndOpenLms() {
        System.out.println("[INFO] Opening Binusmaya (" + BINUSMAYA_URL + ")...");
        driver.get(BINUSMAYA_URL);

        // Check if login is needed
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("login.microsoftonline.com") || currentUrl.contains("login")) {
            System.out.println("\n[ACTION REQUIRED] Please log in with your Binus account in Chrome.\n");
        }

        // Instantly wait for and click the LMS button
        WebElement lmsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//img[@alt='LMS'] | //p[normalize-space()='LMS'] | //div[contains(@class,'MuiGrid-item')][.//img[@alt='LMS'] or .//p[text()='LMS']]")));

        String originalWindow = driver.getWindowHandle();
        clickElement(lmsButton);

        // Switch immediately if LMS opens in a new tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    /**
     * Instant click with fast JavaScript fallback.
     */
    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        File envFile = new File(".env");
        if (envFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith(key + "=")) {
                        return line.substring((key + "=").length()).trim();
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to read .env file: " + e.getMessage());
            }
        }
        return null;
    }
}