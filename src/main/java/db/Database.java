package db;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.mysqlclient.MySQLAuthenticationPlugin;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static final int PORT = 3309;
    private static final String HOST = "192.168.99.100";
    private static final String DB = "dev";
    private static final String USER = "dev"; //dev
    private static final String PASS = "secret"; //secret
    private static final int MAX_POOL_SIZE = 5;

    public static MySQLPool getMySQLClient(Vertx vertx) {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(PORT)
                .setHost(HOST)
                .setDatabase(DB)
                .setUser(USER)
                .setPassword(PASS)
                .setAuthenticationPlugin(MySQLAuthenticationPlugin.DEFAULT);

        PoolOptions poolOptions = new PoolOptions().setMaxSize(MAX_POOL_SIZE);

        MySQLPool client = MySQLPool.pool(vertx, connectOptions, poolOptions);

        client.preparedQuery(
                "CREATE TABLE IF NOT EXISTS user (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(32), password VARCHAR(32), createdDate DATETIME);"
                )
                .execute()
                .compose(res -> client.preparedQuery(
                        "CREATE TABLE IF NOT EXISTS service (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,  name VARCHAR(32), url VARCHAR(128), status VARCHAR(32), createdDate DATETIME, lastUpdated DATETIME, user_id BIGINT, CONSTRAINT fk_service_user_id FOREIGN KEY (user_id) REFERENCES user(id));"
                        )
                        .execute())
                .onComplete(result -> {
                    if (result.succeeded()) {
                        logger.info("completed create db tables");
                    } else {
                        logger.error("Database connection is failed", result.cause());
                    }
                });

        return client;
    }
}
