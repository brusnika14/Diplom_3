package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By nameField = By.xpath("//label[contains(text(),'Имя')]/following-sibling::input");
    private final By emailField = By.xpath("//label[contains(text(),'Email')]/following-sibling::input");
    private final By passwordField = By.xpath("//label[contains(text(),'Пароль')]/following-sibling::input");
    private final By registerButton = By.xpath("//button[contains(text(),'Зарегистрироваться')]");
    private final By passwordError = By.xpath("//p[contains(text(),'Некорректный пароль')]");
    private final By successMessage = By.xpath("//div[contains(@class, 'notification_status_ok')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Step("Заполнение формы регистрации")
    public void fillRegistrationForm(String name, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
    }

    @Step("Нажатие кнопки регистрации")
    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
    }

    @Step("Полная регистрация пользователя")
    public void register(String name, String email, String password) {
        fillRegistrationForm(name, email, password);
        clickRegisterButton();
    }

    @Step("Проверка отображения ошибки пароля")
    public boolean isPasswordErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordError)).isDisplayed();
    }

    @Step("Проверка успешной регистрации")
    public boolean isRegisterSuccess() {
        try {
            // Проверяем появление success-нотификации или редирект
            return wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(successMessage),
                    ExpectedConditions.urlContains("/login")
            ));
        } catch (Exception e) {
            return false;
        }
    }
}