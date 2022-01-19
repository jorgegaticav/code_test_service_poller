package injection;

import dagger.BindsInstance;
import dagger.Component;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.mysqlclient.MySQLPool;
import poller.BackgroundPoller;
import rest.ServiceRouter;

@Component(modules = AppModule.class)
public interface Factory{

    ServiceRouter serviceRouter();

    BackgroundPoller backgroundPoller();

    MySQLPool mySQLClient();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder injectVertx(Vertx vertx);

        @BindsInstance
        Builder injectRouter(Router router);

        @BindsInstance
        Builder injectWebClient(WebClient webClient);

        Factory build();
    }
}
