package rest;

import domain.Service;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import repository.IServiceRepository;

import javax.inject.Inject;

public class ServiceHandler implements IServiceHandler {

    private final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    @Inject
    ServiceHandler() {
    }

    @Inject
    IServiceRepository serviceRepository;

    @Override
    public Handler<RoutingContext> getAllServices() {
        return routingContext -> {
            logger.debug("REST request to get all Services ");
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            serviceRepository.findAllServices()
                    .onSuccess(
                            res -> {
                                logger.debug("Result: " + res);
                                response.setStatusCode(200).end(Json.encodePrettily(res));
                            })
                    .onFailure(
                            err -> logger.error("Error occurred while executing getAllServices", err)
                    );
        };
    }

    @Override
    public Handler<RoutingContext> createService() {
        return routingContext -> {
            HttpServerResponse response = routingContext.response();
            JsonObject body = routingContext.getBodyAsJson();
            Service service = body.mapTo(Service.class);
            logger.debug("REST request to save Service : " + service);
            response.putHeader("content-type", "application/json");
            Future<Service> res = serviceRepository.saveService(service);
            res
                    .onSuccess(
                            resu -> {
                                logger.debug("Saved: " + resu);
                                response.setStatusCode(201).end(Json.encodePrettily(resu));
                            })
                    .onFailure(
                            err -> logger.error("Error occurred while executing createService", err)
                    );
        };
    }

    @Override
    public Handler<RoutingContext> updateService() {
        return routingContext -> {
            JsonObject body = routingContext.getBodyAsJson();
//            Service service = body.mapTo(Service.class);
            Long id = Long.parseLong(routingContext.request().getParam("id"));
            logger.debug("REST request to update Service : " + id);
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            serviceRepository.updateService(id, body)
                    .onSuccess(
                            res -> {
                                logger.info(res);
                                response.setStatusCode(204).end(Json.encodePrettily(true));
                            })
                    .onFailure(
                            err -> logger.error("Error occurred while executing updateService", err)
                    );
        };
    }

    @Override
    public Handler<RoutingContext> getService() {
        return routingContext -> {
            HttpServerResponse response = routingContext.response();
            Long id = Long.parseLong(routingContext.request().getParam("id"));
            logger.debug("REST request to get Service : " + id);
            response.putHeader("content-type", "application/json");
            serviceRepository.findService(id)
                    .onSuccess(
                            res -> {
                                logger.debug("GET: " + res);
                                if(res != null){
                                    response.setStatusCode(200).end(Json.encodePrettily(res));
                                }else{
                                    response.setStatusCode(404).end();
                                }

                            })
                    .onFailure(
                            err -> logger.error("Error occurred while executing getService", err)
                    );
        };
    }

    @Override
    public Handler<RoutingContext> deleteService() {
        return routingContext -> {
            HttpServerResponse response = routingContext.response();
            Long id = Long.parseLong(routingContext.request().getParam("id"));
            logger.debug("REST request to delete Service : " + id);
            response.putHeader("content-type", "application/json");
            serviceRepository.deleteService(id)
                    .onSuccess(
                            res -> {
                                logger.debug("Deleted: " + res);
                                if(res){
                                    response.setStatusCode(204).end(Json.encodePrettily(res));
                                }else{
                                    response.setStatusCode(404).end();
                                }

                            })
                    .onFailure(
                            err -> logger.error("Error occurred while executing deleteService", err)
                    );
        };
    }
}
