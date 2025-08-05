package ru.prakticum.utils;



import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class DriverFactory extends ExternalResource {
    private WebDriver driver;

    @Override
    protected void before() throws Throwable {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10))
                .pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }

    @Override
    protected void after() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver не был инициализирован");
        }
        return driver;
    }
}