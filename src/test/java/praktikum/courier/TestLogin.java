package praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.HttpURLConnection;
import static org.hamcrest.CoreMatchers.equalTo;

/*
 Логин курьера
 Проверь:
 - курьер может авторизоваться;
 - для авторизации нужно передать все обязательные поля;
 - система вернёт ошибку, если неправильно указать логин или пароль;
 - если какого-то поля нет, запрос возвращает ошибку;
 - если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
 - успешный запрос возвращает id.
 * */

public class TestLogin {
    private Courier courier;
    private CourierClient client;
    private CourierChecks check;
    private Credentials cred;
    private int courierId;
    public static final String invalidUsername = "snikers";


    @Before
    @Step("Данные для создания курьера")
    public void createCourierLWith() {
        client = new CourierClient();
        courier = Courier.random();
        client.createCourier(courier);
        cred = Credentials.fromCourier(courier);
        check = new CourierChecks();
    }

    @Test
    @DisplayName("Авторизация курьера c корректными данными")
    public void successfulLoginCourier() {
        ValidatableResponse responseLoginCourier = client.logIn(cred);
        courierId = responseLoginCourier.extract().path("id");
        check.loginSuccess(responseLoginCourier);
        System.out.println("Авторизовался курьер с ID: " + courierId);
    }

    @Test
    @DisplayName("Авторизация курьера без заполнения логина")
    public void courierWithoutLogin() {
        Credentials credWithoutLogin = new Credentials ("", Courier.random().getPassword());
        ValidatableResponse courierWithoutLogin = client.logIn(credWithoutLogin)
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        courierWithoutLogin.assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без ввода пароля")
    public void courierWithoutPassword() {
        Credentials credentialsWithoutLogin = new Credentials(Courier.random().getLogin(), "");
        ValidatableResponse passwordErrorMessage = client.logIn(credentialsWithoutLogin).statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        passwordErrorMessage.assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Авторизация без логина и пароля")
    public void courierWithoutLoginAndPassword() {
        Credentials credWithoutLoginAndPassword = new Credentials("", "");
        ValidatableResponse courierWithoutLoginAndPassword = client.logIn(credWithoutLoginAndPassword)
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        courierWithoutLoginAndPassword.assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с несуществующим логином")
    public void courierNonExistentLogin() {
        Credentials credentialsWithNotExistingLogin = new Credentials(invalidUsername, Courier.random().getPassword());
        ValidatableResponse courierNonExistentLogin = client.logIn(credentialsWithNotExistingLogin)
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND);
        courierNonExistentLogin.assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    @Step("Удалить курьера")
    public void deleteCourier() {
        if (courierId >= 0) {
            client.delete(courierId);
        }
    }
}
