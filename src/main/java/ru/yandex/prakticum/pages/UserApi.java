package ru.yandex.prakticum.pages;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.yandex.prakticum.pages.dto.CreateUserRequest;
import ru.yandex.prakticum.pages.dto.UserLoginRequest;

public class UserApi {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    private String token;

    @Step("Создание нового пользователя")
    public Response registerUser(String email, String password, String name) {
        CreateUserRequest userRequest = new CreateUserRequest(email, password, name);

        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(userRequest)  // Сериализация объекта в JSON
                .post(BASE_URL + "/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(String email, String password) {
        UserLoginRequest loginRequest = new UserLoginRequest(email, password);

        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(loginRequest)  // Сериализация объекта в JSON
                .post(BASE_URL + "/auth/login");
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        RestAssured.given()
                .header("Authorization", token)
                .delete(BASE_URL + "/auth/user");
    }
    @Step("Получение токена из ответа сервера")
    public String getTokenFromResponse(Response response) {
        return response.then().extract().path("accessToken");
    }
}