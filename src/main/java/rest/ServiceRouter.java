package rest;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import javax.inject.Inject;

public class ServiceRouter {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRouter.class);

    @Inject
    ServiceRouter() {
    }

    @Inject
    public Router router;

    @Inject
    IServiceHandler handler;

    public void setRoutes() {
        router.route().handler(BodyHandler.create());

        logger.info("Initializing routes");
        router.get("/").handler(handler -> {
            logger.debug("Request to access root path");
            HttpServerResponse response = handler.response();
            response.putHeader("content-type", "text/plain");
            response.setChunked(true);
            response.end("Kry Service Poller");
        });

        router.get("/services/:id").handler(
                handler.getService()
        );

        router.get("/services").handler(
                handler.getAllServices()
        );

        router.put("/services/:id").handler(
                handler.updateService()
        );

        router.post("/services").handler(
                handler.createService()
        );

        router.delete("/services/:id").handler(
                handler.deleteService()
        );
    }
}
