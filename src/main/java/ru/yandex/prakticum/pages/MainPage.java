package ru.yandex.prakticum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By loginAccountButton = By.xpath("//button[text()='Войти в аккаунт']");
    private final By orderButton = By.xpath("//button[text()='Оформить заказ']");
    private final By personalAccountButton = By.xpath("//a[@href='/account']");
    private final By registerLink = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Зарегистрироваться']");
    private final By loginLinkOnRegisterPage = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");
    private final By forgotPasswordLink = By.xpath("//a[text()='Восстановить пароль']");
    private final By loginLinkOnPasswordRecovery = By.xpath("//a[text()='Войти']");
    private final By bunSection = By.xpath(".//span[text()='Булки']");
    private final By sauceSection = By.xpath(".//span[text()='Соусы']");
    private final By toppingSection = By.xpath(".//span[text()='Начинки']");
    private final By activeSection = By.xpath("//div[contains(@class, 'current')]/span");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Step("Открыть главную страницу")
    public void open() {
        driver.get(Constants.BASE_URL);
        wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.readyState").equals("complete"));
    }

    // Методы для работы с авторизацией
    @Step("Нажать кнопку 'Войти в аккаунт'")
    public void clickLoginButton() {
        clickElement(loginAccountButton);
    }

    @Step("Нажать кнопку 'Личный кабинет'")
    public void clickPersonalAccountButton() {
        clickElement(personalAccountButton);
    }

    @Step("Нажать ссылку 'Зарегистрироваться'")
    public void clickRegisterLink() {
        clickElement(registerLink);
    }

    @Step("Нажать ссылку 'Войти' на странице регистрации")
    public void clickLoginLinkOnRegisterPage() {
        clickElement(loginLinkOnRegisterPage);
    }

    @Step("Нажать ссылку 'Восстановить пароль'")
    public void clickForgotPasswordLink() {
        clickElement(forgotPasswordLink);
    }

    @Step("Нажать ссылку 'Войти' на странице восстановления пароля")
    public void clickLoginLinkOnPasswordRecovery() {
        clickElement(loginLinkOnPasswordRecovery);
    }

    // Методы для работы с разделами конструктора
    @Step("Перейти в раздел 'Булки'")
    public void goToBunSection() {
        switchSection(bunSection, "Булки");
    }

    @Step("Перейти в раздел 'Соусы'")
    public void goToSauceSection() {
        switchSection(sauceSection, "Соусы");
    }

    @Step("Перейти в раздел 'Начинки'")
    public void goToToppingSection() {
        switchSection(toppingSection, "Начинки");
    }

    @Step("Получить название активного раздела")
    public String getActiveSectionName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeSection)).getText();
    }

    @Step("Проверить видимость кнопки 'Оформить заказ'")
    public boolean isOrderButtonVisible() {
        return isElementVisible(orderButton);
    }

    @Step("Проверить, что активен раздел '{sectionName}'")
    public boolean isSectionActive(String sectionName) {
        return sectionName.equals(getActiveSectionName());
    }

    // Приватные вспомогательные методы
    private void clickElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private boolean isElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
    }

    private void switchSection(By sectionLocator, String expectedSectionName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(sectionLocator));
        new Actions(driver)
                .moveToElement(element)
                .click()
                .perform();
        wait.until(d -> expectedSectionName.equals(getActiveSectionName()));
    }
}