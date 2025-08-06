package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы для кнопок авторизации
    private final By loginAccountButton = By.xpath("//button[text()='Войти в аккаунт']");
    private final By orderButton = By.xpath("//button[text()='Оформить заказ']");
    private final By personalAccountButton = By.xpath("//a[@href='/account']");
    private final By registerLink = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Зарегистрироваться']");
    private final By loginLinkOnRegisterPage = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");
    private final By forgotPasswordLink = By.xpath("//a[text()='Восстановить пароль']");
    private final By loginLinkOnPasswordRecovery = By.xpath("//a[text()='Войти']");

    // Локаторы для разделов конструктора
    private final By sectionBun = By.xpath(".//span[text()='Булки']");
    private final By sectionSauce = By.xpath(".//span[text()='Соусы']");
    private final By sectionTopping = By.xpath(".//span[text()='Начинки']");

    // Локаторы для активных элементов
    private final By activeSectionLocator = By.xpath("//div[contains(@class, 'tab_tab__1SPyG') and contains(@class, 'current')]//span");
    private final By currentSectionSpan = By.xpath(".//div[contains(@class, 'current')]/span");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Step("Клик по кнопке войти в аккаунт")
    public void clickLoginAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginAccountButton)).click();
    }

    @Step("Клик по кнопке личный кабинет")
    public void clickPersonalAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton)).click();
    }

    @Step("Клик по ссылке регистрации")
    public void clickRegisterLink() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }

    @Step("Клик по ссылке входа на странице регистрации")
    public void clickLoginLinkOnRegisterPage() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLinkOnRegisterPage)).click();
    }

    @Step("Клик по ссылке восстановления пароля")
    public void clickForgotPasswordLink() {
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
    }

    @Step("Клик по ссылке входа на странице восстановления пароля")
    public void clickLoginLinkOnPasswordRecovery() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLinkOnPasswordRecovery)).click();
    }

    @Step("Нажатие на раздел Булки")
    public void sectionBunClick() {
        clickSection(sectionBun, "Булки");
    }

    @Step("Нажатие на раздел Соусы")
    public void sectionSauceClick() {
        clickSection(sectionSauce, "Соусы");
    }

    @Step("Нажатие на раздел Начинки")
    public void sectionToppingClick() {
        clickSection(sectionTopping, "Начинки");
    }

    private void clickSection(By sectionLocator, String expectedSectionName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(sectionLocator));
        new Actions(driver)
                .moveToElement(element)
                .click()
                .perform();
        wait.until(d -> expectedSectionName.equals(getActiveSectionName()));
    }

    @Step("Получение названия активного раздела")
    public String getActiveSectionName() {
        WebElement activeSection = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSectionLocator));
        return activeSection.getText();
    }

    @Step("Проверка видимости кнопки заказа")
    public boolean isOrderButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderButton)).isDisplayed();
    }

    @Step("Проверка текущего активного раздела")
    public String returnSelectedSection(String sectionName) {
        wait.until(ExpectedConditions.textToBe(currentSectionSpan, sectionName));
        return driver.findElement(currentSectionSpan).getText();
    }
}