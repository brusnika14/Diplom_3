package ru.prakticum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.prakticum.utils.DriverFactory;
import ru.yandex.prakticum.pages.AuthPage;
import ru.yandex.prakticum.pages.MainPage;
import ru.yandex.prakticum.pages.RegisterPage;
import ru.yandex.prakticum.pages.UserApi;

import java.time.Duration;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static ru.yandex.prakticum.pages.Constants.BASE_URL;

public class RegisterTest {
    @Rule
    public TestRule driverRule = new DriverFactory();
    private WebDriver driver;
    private WebDriverWait wait;
    private final Random random = new Random();
    private UserApi userApi;
    private String authToken;
    private String testEmail;

    @Before
    public void setUp() {
        driver = ((DriverFactory) driverRule).getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized");
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        userApi = new UserApi();
    }

    private String generateRandomName() {
        String[] names = {"Анна", "Иван", "Мария", "Алексей", "Елена", "Дмитрий", "Ольга", "Сергей"};
        return names[random.nextInt(names.length)];
    }

    private String generateRandomEmail() {
        String[] domains = {"mail.ru", "gmail.com", "yandex.ru", "hotmail.com"};
        return "user" + random.nextInt(1000000) + "@" + domains[random.nextInt(domains.length)];
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 6 + random.nextInt(5); // Пароль от 6 до 10 символов
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка возможности регистрации пользователя с валидными данными")
    public void testRegistrationAndLogin() {
        String randomName = generateRandomName();
        testEmail = generateRandomEmail();
        String generatedPassword = generateRandomPassword();

        MainPage mainPage = new MainPage(driver);
        AuthPage authPage = new AuthPage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        driver.get(BASE_URL);
        mainPage.clickLoginAccountButton();
        authPage.clickRegisterLink();
        registerPage.register(randomName, testEmail, generatedPassword);

        // Получаем токен для последующего удаления
        Response loginResponse = userApi.loginUser(testEmail, generatedPassword);
        authToken = userApi.getTokenFromResponse(loginResponse);

        wait.until(d -> driver.getCurrentUrl().contains("/login"));
        authPage.login(testEmail, generatedPassword);
        wait.until(d -> mainPage.isOrderButtonVisible());
    }

    @Test
    @DisplayName("Ошибка регистрации")
    @Description("Проверка ошибки при попытке регистрации пользователя с паролем менее 6 символов")
    public void testPasswordValidationWithRandomData() {
        MainPage mainPage = new MainPage(driver);
        AuthPage authPage = new AuthPage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        String name = "ТестовоеИмя";
        testEmail = "test" + System.currentTimeMillis() + "@example.com";
        String invalidPassword = "12345";

        driver.get(BASE_URL);
        mainPage.clickLoginAccountButton();
        authPage.clickRegisterLink();
        registerPage.register(name, testEmail, invalidPassword);

        wait.until(d -> registerPage.isPasswordErrorDisplayed());
        assertTrue("Ожидалась ошибка валидации пароля", registerPage.isPasswordErrorDisplayed());
    }

    @After
    public void tearDown() {
        try {
            // Удаляем пользователя через API, если он был создан
            if (authToken != null && testEmail != null) {
                userApi.deleteUser(authToken);
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}