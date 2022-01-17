package repository;

import domain.Service;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface IServiceRepository {

    Future<Service> findService(Long id);

    Future<List<Service>> findAllServices();

    Future<Service> saveService(Service service);

    Future<Void> updateService(Long id, JsonObject service);

    Future<Void> updateServiceStatus(Long id, String status);

    Future<Boolean> deleteService(Long id);
}
