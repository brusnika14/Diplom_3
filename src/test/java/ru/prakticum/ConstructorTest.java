package ru.prakticum;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.prakticum.utils.DriverFactory;
import ru.yandex.prakticum.pages.MainPage;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static ru.yandex.prakticum.pages.Constats.BASE_URL;

public class ConstructorTest {
    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private WebDriver driver;
    private MainPage mainPage;
    private WebDriverWait wait;  // Объявляем wait как поле класса

    @Before
    public void setUp() {
        // Получаем драйвер из DriverFactory
        driver = driverFactory.getDriver();

        // Инициализируем WebDriverWait с таймаутом 15 секунд
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Инициализация страницы
        mainPage = new MainPage(driver);

        // Открытие сайта
        driver.get(BASE_URL);

        // Ожидание загрузки страницы
        wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.readyState").equals("complete"));
    }

    @Test
    @DisplayName("Переход к разделу Булки")
    @Description("Проверка возможности перехода к разделу Булки на главной странице")
    public void SwitchingToSectionBun() {
        // 1. Убедимся, что начальное состояние - не "Булки"
        if ("Булки".equals(mainPage.getActiveSectionName())) {
            mainPage.sectionToppingClick();
            wait.until(d -> "Начинки".equals(mainPage.getActiveSectionName()));
        }

        // 2. Кликаем на "Булки"
        mainPage.sectionBunClick();

        // 3. Проверяем с явным ожиданием
        String actualText = wait.until(d -> {
            String current = mainPage.getActiveSectionName();
            return "Булки".equals(current) ? current : null;
        });

        assertEquals("Активный раздел должен быть 'Булки'", "Булки", actualText);
    }

    @Test
    @Description("Проверка возможности перехода к разделу Начинки на главной странице")
    public void SwitchingToSectionTopping() {
        mainPage.sectionToppingClick();
        String expectedText = "Начинки";
        String actualText = mainPage.getActiveSectionName();
        assertEquals("Не произошел переход к нужному разделу", expectedText, actualText);
    }

    @Test
    @DisplayName("Переход к разделу Соусы")
    @Description("Проверка возможности перехода к разделу Соусы на главной странице")
    public void SwitchingToSectionSauce() {
        mainPage.sectionSauceClick();
        String expectedText = "Соусы";
        String actualText = mainPage.getActiveSectionName();
        assertEquals("Не произошел переход к нужному разделу", expectedText, actualText);
    }



    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}