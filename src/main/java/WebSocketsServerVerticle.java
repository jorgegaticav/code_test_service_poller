import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class WebSocketsServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketsServerVerticle.class);

    private final Integer WERBSOCKETS_PORT = 8000;

    @Override
    public void start() {
        startServer(vertx);
    }

    private  void startServer(Vertx vertx){
        HttpServer server = vertx.createHttpServer();
        server.webSocketHandler((ctx) -> vertx.eventBus().consumer("services_updates").handler(m -> {
            ctx.writeTextMessage(m.body().toString());
            logger.info("sent: " + m.body().toString());
        })).listen(WERBSOCKETS_PORT);
    }
}
