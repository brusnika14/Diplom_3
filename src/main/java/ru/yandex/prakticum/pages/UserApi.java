package ru.yandex.prakticum.pages;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserApi {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    private String token;

    @Step("Создание нового пользователя")
    public Response registerUser(String email, String password, String name) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(String.format("{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
                        email, password, name))
                .post(BASE_URL + "/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(String email, String password) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password))
                .post(BASE_URL + "/auth/login");
    }
    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        RestAssured.given()
                .header("Authorization", token)
                .delete(BASE_URL + "/auth/user");
    }

    public String getTokenFromResponse(Response response) {
        return response.then().extract().path("accessToken");
    }
}