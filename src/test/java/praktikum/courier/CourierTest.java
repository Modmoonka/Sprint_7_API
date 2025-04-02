package praktikum.courier;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/*
 Создание курьера
 Проверь:
- курьера можно создать;
- нельзя создать двух одинаковых курьеров;
- чтобы создать курьера, нужно передать в ручку все обязательные поля;
- запрос возвращает правильный код ответа;
- успешный запрос возвращает ok: true;
- если одного из полей нет, запрос возвращает ошибку;
- если создать пользователя с логином, который уже есть, возвращается ошибка.
 * */

public class CourierTest {
    private Courier courier;
    private CourierClient client;
    private CourierChecks check;
    private int courierId;

    @Before
    @Step("Данные для создания курьера")
    public void createdCourier() {
        check = new CourierChecks();
        courier = Courier.random();
        client = new CourierClient();
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void courier() {
        ValidatableResponse createResponse = client.createCourier(courier);
        check.created(createResponse);

        Credentials creds = Credentials.fromCourier(courier);
        ValidatableResponse loginResponse = client.logIn(creds);
        courierId = check.loginSuccess(loginResponse);

        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Создать курьера с пустым логином")
    public void courierWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse responseNullLogin = client.createCourier(courier);
        check.failedCreation(responseNullLogin);
    }

    @Test
    @DisplayName("Создать курьера без пароля")
    public void courierdWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse responseNullPassword = client.createCourier(courier);
        check.failedCreation(responseNullPassword);
    }

    @Test
    @DisplayName("Создать курьера без пароля и логина")
    public void courierWithoutLoginAndPassword() {
        courier.setLogin(null);
        courier.setPassword(null);
        ValidatableResponse responseNullFields = client.createCourier(courier);
        check.failedCreation(responseNullFields);
    }

    @Test
    @DisplayName("Создать курьера с уже существующими данными")
    public void courierWithChecks() {
        client.createCourier(courier);
        ValidatableResponse responseCreateCourier = client.createCourier(courier);
        check.loginIsExists(responseCreateCourier);
    }

    @After
    @Step("Удаление курьера")
    public void deleteCourier() {
        if (courierId  >= 0) {
            client.delete(courierId);
        }
    }
}
