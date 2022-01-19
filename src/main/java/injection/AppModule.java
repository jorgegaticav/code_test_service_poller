package injection;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import db.Database;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLPool;
import repository.IServiceRepository;
import repository.ServiceRepository;
import rest.IServiceHandler;
import rest.ServiceHandler;

@Module
public interface AppModule {

    @Provides
    static MySQLPool mySQLPool(Vertx vertx) {
        return Database.getMySQLClient(vertx);
    }

    @Binds
    IServiceHandler bindReviewHandler(ServiceHandler serviceHandler);

    @Binds
    IServiceRepository bindServiceRepository(ServiceRepository serviceRepository);

}
