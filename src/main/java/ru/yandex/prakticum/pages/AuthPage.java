package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthPage {
    private final WebDriver driver;

    // Локаторы
    private final By registerLink = By.xpath("//*[contains(@class, 'Auth_link__1fOlj') and text()='Зарегистрироваться']");
    private final By loginButton = By.xpath("//button[contains(text(),'Войти')]");
    private final By emailField = By.xpath("//label[contains(text(),'Email')]/following-sibling::input");
    private final By passwordField = By.xpath("//label[contains(text(),'Пароль')]/following-sibling::input");


    public AuthPage(WebDriver driver) {
        this.driver = driver;
    }
@Step("Нажмите на ссылку для регистрации")
    public void clickRegisterLink() {
        driver.findElement(registerLink).click();
    }


    @Step("Вход в аккаунт")
    public void login(String email, String password) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }
}