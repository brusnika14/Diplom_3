package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {
    private final WebDriver driver;

    // Локаторы
    private final By nameField = By.xpath("//label[contains(text(),'Имя')]/following-sibling::input");
    private final By emailField = By.xpath("//label[contains(text(),'Email')]/following-sibling::input");
    private final By passwordField = By.xpath("//label[contains(text(),'Пароль')]/following-sibling::input");
    private final By registerButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Зарегистрироваться']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }
@Step("Регистрация")
    public void register(String name, String email, String password) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(registerButton).click();
    }

    public boolean isPasswordErrorDisplayed() {
        return !driver.findElements(By.xpath("//p[contains(@class,'input__error') and contains(text(),'Некорректный пароль')]")).isEmpty();
    }
}