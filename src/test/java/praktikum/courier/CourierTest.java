package praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class CourierTest {
    private CourierClient client = new CourierClient();
    private CourierChecks check = new CourierChecks();
    private int courierId;


    @After
    public void deleteCourier() {
        if (courierId > 0) {
            client.delete(courierId);
        }
    }

    @Test
    @DisplayName("Create new courier")
    @Description("The courier can be created")
    public void courier() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.created(createResponse);

        var creds = Credentials.fromCourier(courier);
        ValidatableResponse loginResponse = client.logIn(creds);
        courierId = check.loginSuccess(loginResponse);

        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Создать курьера с пустым логином")
    @Description("Курьера нельзя создать без логина")
    public void courierWithoutLogin() {
        var courier = Courier.random();
        courier.setLogin(null);
        ValidatableResponse responseNullLogin = client.createCourier(courier);
        check.failedCreation(responseNullLogin);
    }

    @Test
    @DisplayName("Создать курьера без пароля")
    @Description("Курьера нельзя создать без пароля")
    public void courierCanNotBeCreatedWithoutPassword() {
        var courier = Courier.random();
        courier.setPassword(null);
        ValidatableResponse responseNullPassword = client.createCourier(courier);
        check.failedCreation(responseNullPassword);
    }

    @Test
    @DisplayName("Создать курьера без пароля и логина")
    @Description("Курьера нельзя создать без пароля и логина")
    public void courierWithoutLoginAndPassword() {
        var courier = Courier.random();
        courier.setLogin(null);
        courier.setPassword(null);
        ValidatableResponse responseNullFields = client.createCourier(courier);
        check.failedCreation(responseNullFields);
    }

    @Test
    @DisplayName("Создать курьера с существующими данными")
    @Description("Создание курьера с существующими данными")
    public void courierCreatedWithChecks() {
        var courier = Courier.random();
        client.createCourier(courier);
        ValidatableResponse responseCreateCourier = client.createCourier(courier);
        check.loginIsExists(responseCreateCourier);
    }
}
