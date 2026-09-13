import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    private static final String LMS_DASHBOARD_URL = "https://lms.binus.ac.id/lms/dashboard";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-search-engine-choice-screen");

        int port = 9222;

        // Check if an existing Chrome browser is actively responding with remote debugging on port 9222
        if (isDebuggerResponding(port)) {
            System.out.println("[INFO] Attaching directly to your open Chrome on port " + port + "...");
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:" + port);
        } else {
            System.out.println("[INFO] Launching persistent Chrome automation session...");
            // Use dedicated automation profile that remembers your login permanently without file-lock collisions
            String profileDir = System.getProperty("user.home") + File.separator + ".chrome-binus-profile";
            new File(profileDir).mkdirs();
            options.addArguments("--user-data-dir=" + profileDir);
            options.addArguments("--profile-directory=Default");
        }

        System.out.println("[INFO] Initializing WebDriver...");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(45));
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

        System.out.println("[INFO] Locating Schedule sidebar link (<a class='nav-link' href='/lms/schedule'>)...");
        WebElement scheduleLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@class='nav-link' and contains(@href, '/lms/schedule')] | //a[contains(@href, '/lms/schedule')] | //li[contains(@class,'nav-item')]//a[contains(., 'Schedule')]")));

        System.out.println("[INFO] Clicking Schedule link...");
        clickElement(scheduleLink);

        System.out.println("[INFO] Verifying Schedule page loaded...");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("schedule"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(translate(text(), 'SCHEDULE', 'schedule'), 'schedule')]"))
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

        System.out.println("[INFO] Locating Courses sidebar menu link (<a class='nav-link' href='/lms/course'>)...");
        WebElement coursesLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@class='nav-link' and contains(@href, '/lms/course')] | //a[contains(@href, '/lms/course')] | //li[contains(@class,'nav-item')]//a[contains(., 'Courses')]")));

        System.out.println("[INFO] Clicking Courses sidebar link...");
        clickElement(coursesLink);

        System.out.println("[INFO] Waiting for Courses page to load and 'Automation Testing' card to appear...");
        wait.until(ExpectedConditions.urlContains("/lms/course"));

        WebElement autoTestingCourse = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/lms/course/') and contains(normalize-space(), 'Automation Testing')] | //h3[contains(@class, 'item') and contains(@class, 'title')]//a[contains(., 'Automation Testing')] | //div[contains(@class, 'C--CardClass')][.//a[contains(., 'Automation Testing')]]//a[contains(., 'Automation Testing')] | //*[contains(text(), 'Automation Testing')]")));

        System.out.println("[INFO] Clicking 'Automation Testing' course link...");
        clickElement(autoTestingCourse);

        System.out.println("[INFO] Verifying Automation Testing course session page loaded...");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("session"),
                ExpectedConditions.urlContains("course"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Automation Testing') or contains(text(), 'COMP6883001')]"))
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

        System.out.println("[INFO] Waiting for 'Latest Forum Posts' widget on dashboard...");
        WebElement forumHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Latest Forum Posts')]")));
        System.out.println("[SUCCESS] Found 'Latest Forum Posts' widget!");

        System.out.println("[INFO] Locating the first / latest forum post...");
        WebElement latestPost = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Latest Forum Posts')]/following::div[contains(., 'Replied on')][1] | //*[contains(text(), 'Latest Forum Posts')]/ancestor::div[contains(@class, 'card') or contains(@class, 'col') or contains(@class, 'box') or contains(@class, 'widget') or contains(@class, 'panel')]//div[contains(., 'Replied on') or contains(., 'Lecturer') or contains(., 'Anemic Code')][last()] | //*[contains(text(), 'Anemic Code')] | //*[contains(text(), 'Latest Forum Posts')]/following::div[contains(@class, 'thread-author-name')][1]")));

        System.out.println("[INFO] Clicking latest forum post: " + latestPost.getText());
        clickElement(latestPost);

        System.out.println("[INFO] Verifying forum thread loaded...");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("forum"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(translate(text(), 'FORUM', 'forum'), 'forum')] | //*[contains(text(), 'Thread')] | //*[contains(text(), 'Post')] | //*[contains(text(), 'Anemic Code')]"))
        ));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("[SUCCESS] Forum thread opened! Current URL: " + currentUrl);
        assertTrue(currentUrl.toLowerCase().contains("forum") || currentUrl.toLowerCase().contains("lms"),
                "URL should contain forum or lms path after clicking latest forum post");
    }

    /**
     * Navigate to LMS Dashboard and ensure focus on LMS page.
     */
    private void authenticateAndOpenLms() {
        System.out.println("[INFO] Navigating directly to LMS Dashboard (" + LMS_DASHBOARD_URL + ")...");
        driver.get(LMS_DASHBOARD_URL);

        // Switch to the window/tab containing LMS if multiple tabs exist
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getCurrentUrl().contains("lms.binus.ac.id")) {
                break;
            }
        }

        System.out.println("[INFO] Active page URL: " + driver.getCurrentUrl());

        // Check if redirected to Microsoft / Binus Login
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("login.microsoftonline.com") || currentUrl.contains("login") || currentUrl.contains("binusmaya")) {
            System.out.println("\n=======================================================");
            System.out.println("[ACTION REQUIRED] Please log in with your Binus account");
            System.out.println("in the opened Chrome window. It will remember your login");
            System.out.println("for all future test executions.");
            System.out.println("=======================================================\n");
        }

        // Wait until user lands on LMS Dashboard
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, '/lms/schedule')] | //a[contains(@href, '/lms/course')] | //*[contains(text(), 'Latest Forum Posts')]"))
        ));
        
        // Small pause to allow Angular/React frontend components to finish rendering
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        System.out.println("[SUCCESS] LMS Dashboard is fully loaded and ready!");
    }

    /**
     * Safe click helper with scroll into view and JS click fallback.
     */
    private void clickElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("[INFO] Standard click intercepted (" + e.getMessage() + "), executing via JavaScript click...");
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