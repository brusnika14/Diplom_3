package ru.prakticum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import ru.prakticum.utils.DriverFactory;
import ru.yandex.prakticum.pages.MainPage;

import static org.junit.Assert.assertTrue;

public class ConstructorTest {
    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private MainPage mainPage;

    @Before
    public void setUp() {
        mainPage = new MainPage(driverFactory.getDriver());
        mainPage.open();
    }

    @Test
    @DisplayName("Переход к разделу Булки")
    @Description("Проверка перехода к разделу Булки")
    public void shouldSwitchToBunSection() {
        // Если булки уже активны, переключаемся на другой раздел
        if (mainPage.isSectionActive("Булки")) {
            mainPage.goToToppingSection();
            assertTrue("Не удалось переключиться на Начинки",
                    mainPage.isSectionActive("Начинки"));
        }

        // Переключаемся на булки
        mainPage.goToBunSection();
        assertTrue("Раздел 'Булки' не стал активным",
                mainPage.isSectionActive("Булки"));
    }

    @Test
    @DisplayName("Переход к разделу Начинки")
    @Description("Проверка возможности перехода к разделу Начинки на главной странице")
    public void shouldSwitchToToppingSection() {
        mainPage.goToToppingSection();
        assertTrue("Раздел 'Начинки' должен быть активным",
                mainPage.isSectionActive("Начинки"));
    }

    @Test
    @DisplayName("Переход к разделу Соусы")
    @Description("Проверка возможности перехода к разделу Соусы на главной странице")
    public void shouldSwitchToSauceSection() {
        mainPage.goToSauceSection();
        assertTrue("Раздел 'Соусы' должен быть активным",
                mainPage.isSectionActive("Соусы"));
    }

    @After
    public void tearDown() {
        if (driverFactory.getDriver() != null) {
            driverFactory.getDriver().quit();
        }
    }
}