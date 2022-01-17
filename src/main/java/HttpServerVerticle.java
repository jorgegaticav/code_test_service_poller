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
import poller.BackgroundPoller;
import rest.ServiceRouter;

import java.time.LocalDateTime;

public class HttpServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    private final Integer HTTP_PORT = 8080;
    private final long DELAY = 8000;

    @Override
    public void start(Promise<Void> startPromise) {

        // Register JavaTimeModule for Vert.x Jackson ObjectMapper to map LocalDateTime
        ObjectMapper mapper = DatabindCodec.mapper();
        mapper.registerModule(new JavaTimeModule());

        ObjectMapper prettyMapper = DatabindCodec.prettyMapper();
        prettyMapper.registerModule(new JavaTimeModule());

        // Initializing router and webclient with vertex var
        Router router = Router.router(vertx);
        WebClient webClient = WebClient.create(vertx);

        // Enable CORS
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

    }
}
