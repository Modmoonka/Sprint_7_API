package praktikum.orders;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.net.HttpURLConnection;
import static org.hamcrest.CoreMatchers.notNullValue;

/*
Создание заказа
Проверь, что когда создаёшь заказ:
- можно указать один из цветов — BLACK или GREY;
- можно указать оба цвета;
- можно совсем не указывать цвет;
- тело ответа содержит track.
 * */

@RunWith(Parameterized.class)
public class OrderTest {
    private OrderList orderList;
    private final String[] color;
    java.lang.Object track;

    public OrderTest(String[] color) {
        this.color = color;
    }

    @Before
    public void orderList() {
        orderList = new OrderList();
    }

    @After
    @Step("Отменить тестовый заказ")
    public void CancelTestOrder() {
        orderList.cancelOrder(track);
    }

    @Parameterized.Parameters(name = "Цвет самоката")
    public static Object[][] getColour() {
        return new Object[][]{
                {new String[]{"black"}},
                {new String[]{"gray"}},
                {new String[]{"black", "gray"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void orderWithDifferentColors() {
        OrderCreate orderCreate = new OrderCreate(color);
        ValidatableResponse responseOrderCreated= orderList.createNewOrder(orderCreate);
        track = responseOrderCreated.extract().path("track");
        responseOrderCreated.assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue());
    }
}
