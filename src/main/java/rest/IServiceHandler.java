package rest;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface IServiceHandler {

    Handler<RoutingContext> getAllServices();

    Handler<RoutingContext> createService();

    Handler<RoutingContext> updateService();

    Handler<RoutingContext> getService();

    Handler<RoutingContext> deleteService();
}
