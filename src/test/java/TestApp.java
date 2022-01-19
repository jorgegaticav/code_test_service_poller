import domain.Service;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestApp {

    private Vertx vertx;
    private WebClient webClient;
    private static final String NAME = "kry";
    private static final String URL = "http://www.kry.se";

    @BeforeEach
    void setup(VertxTestContext testContext) {
        this.vertx = Vertx.vertx();
        webClient = WebClient.create(vertx);
        vertx.deployVerticle(
                new HttpServerVerticle(),
                testContext.succeeding(id -> testContext.completeNow()));

    }

    @AfterEach
    public void finish(VertxTestContext testContext) {
        vertx.close(testContext.succeeding(res -> testContext.completeNow()));
    }

    @Test
    @DisplayName("Post request on localhost:8080/services")
    @Order(1)
    void postRequest(VertxTestContext testContext) {
        JsonObject json = new JsonObject()
                .put("name", NAME)
                .put("url", URL);

        webClient.post(8080,"localhost", "/services")
                .sendJsonObject(json, ar -> {
                    assert (ar.succeeded());
                    System.out.println(ar.result().body().toString());
                    testContext.completeNow();
                });
    }

    @Test
    @DisplayName("Get request on localhost:8080/services")
    @Order(2)
    void getRequest(VertxTestContext testContext) {
        webClient
                .get(8080, "localhost", "/services")
                .send(response -> testContext.verify(() -> {
                    assert (200 == response.result().statusCode());
                    System.out.println(response.result().body());
                    testContext.completeNow();
                }));
    }


//    @Test
//    @DisplayName("Put request on localhost:8080/services/lastInsertedId")
//    @Order(3)
//    void putRequest(VertxTestContext testContext) {
//        JsonObject json = new JsonObject()
//                .put("name", PUT_NAME)
//                .put("url", PUT_URL);
//
//        webClient.put(8080,"localhost", "/services/"+lastInsertedId)
//                .sendJsonObject(json, ar -> {
//                    assert (ar.succeeded());
//                    System.out.println("Put request on localhost:8080/services/"+lastInsertedId);
//                    testContext.completeNow();
//                });
//    }

//    @Test
//    @DisplayName("Delete request on localhost:8080/services/lastInsertedId")
//    @Order(4)
//    void deleteRequest(VertxTestContext testContext) {
//        System.out.println("Delete request on localhost:8080/services/ " + lastInsertedId);
//        webClient.delete(8080,"localhost", "/services/" + lastInsertedId)
//                .send(res -> {
//                    System.out.println(res);
//            assert (res.result().body().toString().equals("true"));
//            testContext.completeNow();
//        });
//    }

}
