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
import ru.prakticum.utils.DriverFactory;
import ru.yandex.prakticum.pages.MainPage;
import ru.yandex.prakticum.pages.RegisterPage;
import ru.yandex.prakticum.pages.UserApi;

import static org.junit.Assert.assertTrue;
import static ru.yandex.prakticum.pages.Constants.BASE_URL;

public class RegisterTest {
    @Rule
    public TestRule driverRule = new DriverFactory();
    private WebDriver driver;
    private UserApi userApi;
    private String authToken;
    private String testEmail;

    @Before
    public void setUp() {
        driver = ((DriverFactory) driverRule).getDriver();
        userApi = new UserApi();
    }

    private String generateTestEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка регистрации с валидными данными")
    public void testSuccessfulRegistration() {
        MainPage mainPage = new MainPage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        testEmail = generateTestEmail();
        String password = "validPassword123";

        // Открываем страницу и переходим к регистрации
        driver.get(BASE_URL);
        mainPage.clickLoginButton();
        mainPage.clickRegisterLink();

        // Регистрируем пользователя
        registerPage.register("Тестовый Пользователь", testEmail, password);

        // Проверяем успешность регистрации
        assertTrue("Не появилось подтверждение успешной регистрации",
                registerPage.isRegisterSuccess());

        // Получаем токен для последующего удаления пользователя
        Response response = userApi.loginUser(testEmail, password);
        authToken = userApi.getTokenFromResponse(response);
    }

    @Test
    @DisplayName("Ошибка при регистрации с коротким паролем")
    @Description("Проверка валидации пароля при регистрации")
    public void testShortPasswordValidation() {
        MainPage mainPage = new MainPage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        testEmail = generateTestEmail();
        String shortPassword = "12345";

        driver.get(BASE_URL);
        mainPage.clickLoginButton();
        mainPage.clickRegisterLink();
        registerPage.register("Тестовый Пользователь", testEmail, shortPassword);

        assertTrue("Ошибка валидации пароля не отображается",
                registerPage.isPasswordErrorDisplayed());
    }

    @After
    public void tearDown() {
        try {
            if (authToken != null) {
                userApi.deleteUser(authToken);
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}