package praktikum.orders;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import java.net.HttpURLConnection;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;

/*
Список заказов
Проверь, что в тело ответа возвращается список заказов.
 * */

public class OrderListTest {
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Успешное получение списка заказов")
    public void getOrderList() {
        OrderList orderList = new OrderList();
        ValidatableResponse responseOrderList = orderList.getOrderList();
        responseOrderList.statusCode(HttpURLConnection.HTTP_OK);
        assertNotNull(responseOrderList.extract().path("orders"));
    }
}
