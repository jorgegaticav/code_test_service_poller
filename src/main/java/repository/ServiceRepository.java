package repository;

import domain.Service;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository implements IServiceRepository {

    private final Logger logger = LoggerFactory.getLogger(ServiceRepository.class);

    @Inject
    ServiceRepository() {
    }

    @Inject
    MySQLPool client;

    @Override
    public Future<Service> findService(Long id) {
        return client.preparedQuery("SELECT * FROM service WHERE id = ?").execute(Tuple.of(id))
                .map(
                        rs -> {
//                            logger.info("got response from DB : " + rs);
                            Service service = null;
                            for (Row r : rs) {
                                try {
                                    service = new Service(
                                            r.getLong("id"),
                                            r.getString("name"),
                                            new URL(r.getString("url")),
                                            r.getString("status"),
                                            r.getLocalDateTime("createdDate"),
                                            r.getLocalDateTime("lastUpdated"));
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
//                            logger.info("Returning service");
                            return service;
                        });

    }

    @Override
    public Future<List<Service>> findAllServices() {
        return client.preparedQuery("SELECT * FROM service").execute().map(
                rows -> {
//                    logger.info("got response from DB");
                    List<Service> services = new ArrayList<>();
                    for (Row rowSet : rows) {
                        try {
                            services.add(new Service(
                                    rowSet.getLong("id"),
                                    rowSet.getString("name"),
                                    new URL(rowSet.getString("url")),
                                    rowSet.getString("status"),
                                    rowSet.getLocalDateTime("createdDate"),
                                    rowSet.getLocalDateTime("lastUpdated")));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    return services;
                });
    }

    @Override
    public Future<Service> saveService(Service service) {
        LocalDateTime now = LocalDateTime.now();

        return client.preparedQuery("INSERT INTO service (name, url, status, createdDate, lastUpdated) VALUES  (?, ?, ?, ?, ?)")
                .execute(Tuple.of(service.getName(), service.getUrl().toString(), "UNKNOWN", now, now)).map(ar -> {
                        long lastInsertedId = ar.property(MySQLClient.LAST_INSERTED_ID);
                        logger.info("last inserted id: " + lastInsertedId);
                        service.setId(lastInsertedId);
                        service.setStatus("UNKNOWN");
                        service.setCreatedDate(now);
                        service.setLastUpdated(now);
                        return service;
                });
    }

    @Override
    public Future<Void> updateService(Long id, JsonObject service) {
        client.preparedQuery("UPDATE service SET name = ?, url = ? WHERE id = ?")
                .execute(Tuple.of(service.getString("name"), service.getString("url"), id));
        return Future.succeededFuture();
    }

    @Override
    public Future<Void> updateServiceStatus(Long id, String status) {
//        logger.info("Updating service with id = "+ id + " to status = " + status);
        client.preparedQuery("UPDATE service SET status = ?, lastUpdated = ? WHERE id = ?")
                .execute(Tuple.of(status, LocalDateTime.now(), id));
        return Future.succeededFuture();

    }

    @Override
    public Future<Boolean> deleteService(Long id) {
        return client.preparedQuery("SELECT EXISTS(SELECT * FROM service where id = ?)")
                .execute(Tuple.of(id))
                .map(
                        rs -> {
                            Integer v = null;
                            for (Row rowSet : rs) {
                                v = rowSet.get(Integer.class, 0);
                            }
                            assert v != null;
                            if (v > 0) {
                                client.preparedQuery("DELETE FROM service WHERE id = ?")
                                        .execute(Tuple.of(id));
                                return true;
                            } else {
                                return false;
                            }
                        });
    }
}
