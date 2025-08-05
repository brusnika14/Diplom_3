package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By emailField = By.xpath("//input[@name='name']");
    private final By passwordField = By.xpath("//input[@name='Пароль']");
    private final By loginButton = By.xpath("//button[text()='Войти']");
    private final By pageTitle = By.xpath("//h2[text()='Вход']");
    private final By registerLink = By.xpath("//a[text()='Зарегистрироваться']");


    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Step("Заполнить Email")
    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        element.clear();
        element.sendKeys(email);
    }
    @Step("Заполнить пароль")
    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        element.clear();
        element.sendKeys(password);
    }
    @Step("Кликнуть по кнопке Войти")
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
    @Step
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }

    public void clickRegisterLink() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }
}