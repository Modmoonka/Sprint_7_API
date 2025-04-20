package praktikum;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static praktikum.EnvConfig.BASE_URI;

public class Order {
    public static final String ORDER = "api/v1/orders";
    public static final String ORDER_CANCEL = "api/v1/orders/cancel";

    public RequestSpecification specOrder() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }
}
