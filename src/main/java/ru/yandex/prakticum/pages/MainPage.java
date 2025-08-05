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

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Локаторы
    private final By loginAccountButton = By.xpath("//button[text()='Войти в аккаунт']");
    private final By orderButton = By.xpath("//button[text()='Оформить заказ']");
    private final By personalAccountButton = By.xpath("//a[@href='/account']");
    // Локатор для кнопки "Зарегистрироваться" на странице входа
    private final By registerLink = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Зарегистрироваться']");

    // Локатор для кнопки "Войти" на странице регистрации
    private final By loginLinkOnRegisterPage = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");

    private final By forgotPasswordLink = By.xpath("//a[text()='Восстановить пароль']");
    private final By loginLinkOnPasswordRecovery = By.xpath("//a[text()='Войти']");

    private final By sectionBun = By.xpath(".//span[text()='Булки']");
    // раздел Соусы
    private final By sectionSauce = By.xpath(".//span[text()='Соусы']");
    // раздел Начинки
    private final By sectionTopping = By.xpath(".//span[text()='Начинки']");

    private final By activeSectionLocator = By.xpath("//div[contains(@class, 'current')]//span");

    @Step("Клик по кнопке войти в аккаунт")
    public void clickLoginAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginAccountButton)).click();
    }
    @Step("Клик по кнопке личный кабинет")
    public void clickPersonalAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton)).click();
    }
    @Step
    public void clickRegisterLink() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }
    @Step("Нажмите на страницу входа в систему и ссылки на регистрацию")
    public void clickLoginLinkOnRegisterPage() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLinkOnRegisterPage)).click();
    }
    @Step("Нажмите на ссылку для получения пароля")
    public void clickForgotPasswordLink() {
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
    }

    @Step("Нажмите кнопку Войти, чтобы восстановить пароль.")
    public void clickLoginLinkOnPasswordRecovery() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLinkOnPasswordRecovery)).click();
    }



    @Step("Нажатие на раздел Булки")
    public void sectionBunClick() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(sectionBun));

        // Прокрутка к элементу и клик через Actions для надёжности
        new Actions(driver)
                .moveToElement(element)
                .click()
                .perform();

        // Ожидание с явной проверкой текста
        wait.until(d -> "Булки".equals(getActiveSectionName()));
    }



    @Step("Нажатие на раздел Соусы")
    public void sectionSauceClick () {
        new WebDriverWait(driver,Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(sectionSauce));
        driver.findElement(sectionSauce).click();
    }

    @Step("Нажатие на раздел Начинки")
    public void sectionToppingClick () {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(sectionTopping));
        driver.findElement(sectionTopping).click();
    }

    @Step("Возвращает выбранный элемент в конструкторе")
    public String returnSelectedSection(String sectionName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(By.xpath(".//div[contains(@class, 'current')]/span"), sectionName));
        return driver.findElement(By.xpath(".//div[contains(@class, 'current')]/span")).getText();
    }
    @Step("Получение названия активного раздела")
    public String getActiveSectionName() {
        // Более точный локатор для активного раздела
        By activeSectionLocator = By.xpath("//div[contains(@class, 'tab_tab__1SPyG') and contains(@class, 'current')]//span");

        WebElement activeSection = wait.until(ExpectedConditions.visibilityOfElementLocated(activeSectionLocator));
        return activeSection.getText();
    }








    public boolean isOrderButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderButton)).isDisplayed();
    }

}