import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;


public class App {

    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpServerVerticle());

//        vertx.deployVerticle(new WebSocketsServerVerticle());
    }

}
