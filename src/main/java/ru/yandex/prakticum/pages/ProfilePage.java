package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProfilePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By profileSection = By.xpath("//a[contains(@class, 'Account_link_active') and text()='Профиль']");
    private final By userNameField = By.xpath("//input[@name='Name']");
    private final By logoutButton = By.xpath("//button[text()='Выход']");



    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Step("Проверка отображения страницы профиля")
    public boolean isProfilePageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(profileSection)).isDisplayed();
    }
    @Step("Проверка отображения имени пользователя: {name}")
    public boolean isUserNameDisplayed(String name) {
        String actualName = wait.until(ExpectedConditions.visibilityOfElementLocated(userNameField))
                .getAttribute("value");
        return name.equals(actualName);
    }
    @Step("Нажатие кнопки 'Выход'")
    public void clickLogoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}