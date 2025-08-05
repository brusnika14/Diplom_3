package ru.prakticum;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.openqa.selenium.WebDriver;

import ru.prakticum.utils.DriverFactory;
import ru.yandex.prakticum.pages.AuthPage;
import ru.yandex.prakticum.pages.MainPage;
import ru.yandex.prakticum.pages.RegisterPage;


import java.util.Random;

import static org.junit.Assert.assertTrue;

public class RegisterTest {
    @Rule
    public TestRule driverRule = new DriverFactory();
    private WebDriver driver;
    private final Random random = new Random();

    @Before
    public void setUp() {
        // Получаем драйвер из DriverFactory
        driver = ((DriverFactory) driverRule).getDriver();
    }

    private String generateRandomName() {
        String[] names = {"Анна", "Иван", "Мария", "Алексей", "Елена", "Дмитрий", "Ольга", "Сергей"};
        return names[random.nextInt(names.length)];
    }

    private String generateRandomEmail() {
        String[] domains = {"mail.ru", "gmail.com", "yandex.ru", "hotmail.com"};
        return "user" + System.currentTimeMillis() + "@" + domains[random.nextInt(domains.length)];
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 6 + random.nextInt(5);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка возможности регистрации пользователя с валидными данными")
    public void testRegistrationAndLogin() throws InterruptedException {
        // Генерация тестовых данных
        String randomName = generateRandomName();
        String generatedEmail = generateRandomEmail();
        String generatedPassword = generateRandomPassword();

        System.out.println("Сгенерированные данные:");
        System.out.println("Имя: " + randomName);
        System.out.println("Email: " + generatedEmail);
        System.out.println("Пароль: " + generatedPassword);

        // Инициализация страниц
        MainPage mainPage = new MainPage(driver);
        AuthPage authPage = new AuthPage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        // 1. Открываем главную страницу
        driver.get("https://stellarburgers.nomoreparties.site/");

        // 2. Переходим в личный кабинет
        mainPage.clickLoginAccountButton();

        // 3. Переходим на форму регистрации
        authPage.clickRegisterLink();

        // 4. Регистрируем нового пользователя
        registerPage.register(randomName, generatedEmail, generatedPassword);
        Thread.sleep(3000);

        // 5. Переходим на страницу входа
        driver.get("https://stellarburgers.nomoreparties.site/login");

        // 6. Входим с созданными данными
        authPage.login(generatedEmail, generatedPassword);
        Thread.sleep(3000);
    }
    @Test
    @DisplayName("Ошибка регистрации")
    @Description("Проверка ошибки при попытке регистрации пользователя с паролем менее 6 символов")
    public void testPasswordValidationWithRandomData() throws InterruptedException {
        // Инициализация страниц
        MainPage mainPage = new MainPage(driver);
        AuthPage authPage = new AuthPage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        // Генерация тестовых данных
        String name = "ТестовоеИмя";
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String invalidPassword = "12345"; // Намеренно неверный пароль

        // Шаги теста
        driver.get("https://stellarburgers.nomoreparties.site/");
        mainPage.clickLoginAccountButton();
        authPage.clickRegisterLink();
        registerPage.register(name, email, invalidPassword);

        // Проверка
        assertTrue("Ожидалась ошибка валидации пароля",
                registerPage.isPasswordErrorDisplayed());

        Thread.sleep(3000);

    }

    @After
    public void tearDown() {
        driver.quit();
    }
}