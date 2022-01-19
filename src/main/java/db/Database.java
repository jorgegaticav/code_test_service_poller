package db;

import io.vertx.core.Future;
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

        return MySQLPool.pool(vertx, connectOptions, poolOptions);

    }
}
