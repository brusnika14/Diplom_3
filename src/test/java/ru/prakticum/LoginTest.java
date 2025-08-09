package ru.prakticum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.rules.TestRule;
import org.openqa.selenium.WebDriver;
import ru.prakticum.utils.DriverFactory;
import ru.yandex.prakticum.pages.*;

import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static ru.yandex.prakticum.pages.Constants.BASE_URL;

public class LoginTest {
    @Rule
    public TestRule driverRule = new DriverFactory();

    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private ProfilePage profilePage;
    private UserApi userAPI;
    private String authToken;

    // Тестовые данные
    private final String TEST_EMAIL = "test-user-" + System.currentTimeMillis() + "@mail.ru";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_NAME = "Test User";

    @Before
    public void setUp() {
        // Получаем драйвер из DriverFactory
        driver = ((DriverFactory) driverRule).getDriver();

        // Инициализация страниц
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
        userAPI = new UserApi();

        // Регистрируем пользователя перед тестами
        Response registerResponse = userAPI.registerUser(TEST_EMAIL, TEST_PASSWORD, TEST_NAME);
        authToken = userAPI.getTokenFromResponse(registerResponse);
    }

    @Test
    @DisplayName("Вход на главной странице по кнопке Войти в аккаунт")
    @Description("Проверка возможности входа в аккаунт после нажатия на кнопку Войти в аккаунт на главной странице")
    public void loginWithValidCredentials() {
        driver.get(BASE_URL);
        mainPage.clickLoginButton(); // Изменено с clickLoginAccountButton()
        assertTrue(loginPage.isLoginPageDisplayed());
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(mainPage.isOrderButtonVisible());
    }

    @Test
    @DisplayName("Вход на главной странице по кнопке Личный кабинет")
    @Description("Проверка возможности входа в аккаунт после нажатия на кнопку Личный кабинет на главной странице")
    public void loginMainPageProfileButton() {
        driver.get(BASE_URL);
        mainPage.clickPersonalAccountButton();
        assertTrue(loginPage.isLoginPageDisplayed());
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(mainPage.isOrderButtonVisible());
        mainPage.clickPersonalAccountButton();
        assertTrue(profilePage.isProfilePageDisplayed());
        assertTrue(profilePage.isUserNameDisplayed(TEST_NAME));
    }

    @Test
    @DisplayName("Вход со страницы регистрации")
    @Description("Проверка возможности входа в аккаунт после нажатия на кнопку Войти на странице регистрации")
    public void loginFromRegistrationPageTest() {
        driver.get(BASE_URL);
        mainPage.clickLoginButton(); // Изменено с clickLoginAccountButton()
        mainPage.clickRegisterLink();
        mainPage.clickLoginLinkOnRegisterPage();
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);
        assertTrue("Кнопка 'Оформить заказ' не отображается после входа",
                mainPage.isOrderButtonVisible());
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Проверка возможности входа через кнопку 'Войти' на странице восстановления пароля")
    public void loginFromPasswordRecoveryPageTest() {
        driver.get(BASE_URL);
        mainPage.clickLoginButton(); // Изменено с clickLoginAccountButton()
        mainPage.clickForgotPasswordLink();
        assertTrue("Не перешли на страницу восстановления пароля",
                Objects.requireNonNull(driver.getCurrentUrl()).contains("/forgot-password"));
        mainPage.clickLoginLinkOnPasswordRecovery();
        assertTrue("Не вернулись на страницу входа",
                driver.getCurrentUrl().contains("/login"));
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);
        assertTrue("Кнопка 'Оформить заказ' не отображается после входа",
                mainPage.isOrderButtonVisible());
    }

    @After
    public void tearDown() {
        try {
            if (authToken != null) {
                userAPI.deleteUser(authToken);
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}