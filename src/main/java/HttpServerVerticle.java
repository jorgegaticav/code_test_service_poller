import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import injection.DaggerFactory;
import injection.Factory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.mysqlclient.MySQLPool;
import poller.BackgroundPoller;
import rest.ServiceRouter;

import java.time.LocalDateTime;

public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    private final Integer HTTP_PORT = 8080;
    private final long DELAY = 8000;

    @Override
    public void start(Promise<Void> startPromise) {

        banner();

        // Register JavaTimeModule for Vert.x Jackson ObjectMapper to map LocalDateTime
        ObjectMapper mapper = DatabindCodec.mapper();
        mapper.registerModule(new JavaTimeModule());

        ObjectMapper prettyMapper = DatabindCodec.prettyMapper();
        prettyMapper.registerModule(new JavaTimeModule());

        // Initializing router and webclient with vertex var
        Router router = Router.router(vertx);
        WebClient webClient = WebClient.create(vertx);

        // Enable CORS`
        router.route().handler(CorsHandler.create("http://localhost:3000")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowCredentials(true)
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Authorization")
                .allowedHeader("Access-Control-Allow-Method")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Content-Type"));

        // Injecting interfaces
        Factory factory = DaggerFactory.builder()
                .injectVertx(vertx)
                .injectRouter(router)
                .injectWebClient(webClient).build();

        MySQLPool client = factory.mySQLClient();

        // Obtaining services with injected vars
        ServiceRouter serviceRouter = factory.serviceRouter();
        BackgroundPoller poller = factory.backgroundPoller();

        // Setting routes
        serviceRouter.setRoutes();

        // Initializing http server
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router).listen(HTTP_PORT);

        // Check services periodically (service polling)
        vertx.setPeriodic(DELAY, (__) -> {
            poller.pollServices();
            vertx.eventBus().send("services_updates", "ping " + LocalDateTime.now());
        });

        createTables(startPromise, client);

    }

    void createTables(Promise<Void> startPromise, MySQLPool client){

        client.preparedQuery(
                        "CREATE TABLE IF NOT EXISTS user (" +
                                "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                                "login VARCHAR(50), " +
                                "password_hash VARCHAR(60), " +
                                "first_name VARCHAR(50)," +
                                "last_name VARCHAR(50)," +
                                "email VARCHAR(191) UNIQUE," +
                                "activated BOOLEAN," +
                                "activation_key VARCHAR(20)," +
                                "reset_key VARCHAR(20)," +
                                "created_date TIMESTAMP);"
                )
                .execute()
                .compose(res -> client.preparedQuery(
                                "CREATE TABLE IF NOT EXISTS service (" +
                                        "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,  " +
                                        "name VARCHAR(32), " +
                                        "url VARCHAR(128), " +
                                        "status VARCHAR(32), " +
                                        "createdDate DATETIME, " +
                                        "lastUpdated DATETIME, " +
                                        "user_id BIGINT, " +
                                        "CONSTRAINT fk_service_user_id FOREIGN KEY (user_id) REFERENCES user(id));"
                        )
                        .execute())
                .onComplete(result -> {
                    if (result.succeeded()) {
                        logger.info("completed create db tables");
                        startPromise.complete();
                    } else {
                        logger.error("Database connection is failed", result.cause());
                        startPromise.fail(result.cause());
                    }
                });
    }

    void banner(){
        String banner =
                "                                d8,                                     d8bd8b                 \n" +
                "                               `8P                                      88P88P                 \n" +
                "                                                                       d88d88                  \n" +
                " .d888b,d8888b  88bd88b?88   d8P88b d8888b d8888b    ?88,.d88b, d8888b 888888   d8888b  88bd88b\n" +
                " ?8b,  d8b_,dP  88P'  `d88  d8P'88Pd8P' `Pd8b_,dP    `?88'  ?88d8P' ?88?88?88  d8b_,dP  88P'  `\n" +
                "   `?8b88b     d88     ?8b ,88'd88 88b    88b          88b  d8P88b  d88 88b88b 88b     d88     \n" +
                "`?888P'`?888P'd88'     `?888P'd88' `?888P'`?888P'      888888P'`?8888P'  88b88b`?888P'd88'     \n" +
                "                                                       88P'                                    \n" +
                "                                                      d88                                      \n" +
                "                                                      ?8P                                      \n\n" +
                ":: Running Vert.x 2.4.3 ::\n\n";
        System.out.println(banner);
    }
}
