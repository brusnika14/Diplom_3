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
        driver.get("https://stellarburgers.nomoreparties.site/");
        mainPage.clickLoginAccountButton();
        assertTrue(loginPage.isLoginPageDisplayed());
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(mainPage.isOrderButtonVisible());
    }

    @Test
    @DisplayName("Вход на главной странице по кнопке Личный кабинет")
    @Description("Проверка возможности входа в аккаунт после нажатия на кнопку Личный кабинет на главной странице")
    public void loginMainPageProfileButton() {
        // 1. Открываем главную страницу
        driver.get("https://stellarburgers.nomoreparties.site/");

        // 2. Кликаем кнопку "Личный кабинет" (должно перенаправить на страницу входа)
        mainPage.clickPersonalAccountButton();

        // 3. Проверяем что открылась страница входа
        assertTrue(loginPage.isLoginPageDisplayed());

        // 4. Заполняем и отправляем форму входа
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);

        // 5. Проверяем успешную авторизацию
        assertTrue(mainPage.isOrderButtonVisible());

        // 6. Переходим в личный кабинет (теперь используем ProfilePage)
        mainPage.clickPersonalAccountButton();

        // 7. Проверяем что открылась страница профиля
        assertTrue(profilePage.isProfilePageDisplayed());

        // 8. Проверяем что отображается имя пользователя
        assertTrue(profilePage.isUserNameDisplayed(TEST_NAME));
    }

    @Test
    @DisplayName("Вход со страницы регистрации")
    @Description("Проверка возможности входа в аккаунт после нажатия на кнопку Войти на странице регистрации")
    public void loginFromRegistrationPageTest() {


        // 1. Открываем главную страницу
        driver.get("https://stellarburgers.nomoreparties.site/");

        // 2. Переходим на страницу входа
        mainPage.clickLoginAccountButton();

        // 3. Переходим на страницу регистрации
        mainPage.clickRegisterLink();

        // 4. Возвращаемся на страницу входа через кнопку "Войти"
        mainPage.clickLoginLinkOnRegisterPage();

        // 5. Заполняем форму входа
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);

        // 6. Проверяем успешную авторизацию
        assertTrue("Кнопка 'Оформить заказ' не отображается после входа",
                mainPage.isOrderButtonVisible());
    }
    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Проверка возможности входа через кнопку 'Войти' на странице восстановления пароля")
    public void loginFromPasswordRecoveryPageTest() {
        // 1. Открываем главную страницу
        driver.get("https://stellarburgers.nomoreparties.site/");

        // 2. Переходим на страницу входа
        mainPage.clickLoginAccountButton();

        // 3. Переходим на страницу восстановления пароля
        mainPage.clickForgotPasswordLink();
        assertTrue("Не перешли на страницу восстановления пароля",
                Objects.requireNonNull(driver.getCurrentUrl()).contains("/forgot-password"));

        // 4. Возвращаемся на страницу входа через кнопку "Войти"
        mainPage.clickLoginLinkOnPasswordRecovery();
        assertTrue("Не вернулись на страницу входа",
                driver.getCurrentUrl().contains("/login"));

        // 5. Заполняем форму входа
        loginPage.login(TEST_EMAIL, TEST_PASSWORD);

        // 6. Проверяем успешную авторизацию
        assertTrue("Кнопка 'Оформить заказ' не отображается после входа",
                mainPage.isOrderButtonVisible());
    }



    @After
    public void tearDown() {
        try {
            // Удаляем пользователя через API
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