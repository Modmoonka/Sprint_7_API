package praktikum.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.net.HttpURLConnection;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;

public class CourierChecks {
    @Step("Успешный логин")
    public int loginSuccess(ValidatableResponse loginResponse) {
        int id = loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("id");
        return id;
    }

    @Step("Успешное создание учетной записи")
    public void created(ValidatableResponse createResponse) {
        boolean created = createResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .path("ok");
        assertTrue(created);
    }

    @Step("Запрос с повторяющимся логином")
    public void loginIsExists(ValidatableResponse response){
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется.")); //Идет не совпадение ОР и ФР
    }

    @Step("Запрос без логина и пароля")
    public void failedCreation(ValidatableResponse response){
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }
}
