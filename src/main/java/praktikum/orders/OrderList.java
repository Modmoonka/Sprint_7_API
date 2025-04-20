package praktikum.orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.Order;

public class OrderList extends Order {
    @Step("Создать заказ")
    public ValidatableResponse createNewOrder(OrderCreate orderCreate){
        return specOrder()
                .body(orderCreate)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrderList(){
        return specOrder()
                .when()
                .get(ORDER)
                .then();
    }

    @Step("Отменить заказ")
    public void cancelOrder(Object track){
        specOrder()
                .body(track)
                .when()
                .put(ORDER_CANCEL)
                .then();
    }
}
