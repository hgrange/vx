package com.ibm.cda.stack.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class Starter {

  private Vertx vertx = null;
  public static void main(String[] args) {
    Starter starter = new Starter();
    starter.launch();
  }
   
  public void launch(){ 
   
    vertx = Vertx.vertx();
    // Create a router object.
    Router router = Router.router(vertx);
    // Add the handler 
    router.get("/hello").handler(req -> hello(req));
    router.get("/health").handler(req -> health(req));
    router.get("/*").handler(StaticHandler.create());
    // Create the HTTP server.
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080, ar -> {
              if (ar.succeeded()) {
                System.out.println("Server started on localhost:" + ar.result().actualPort());
              }
            });
  }

  public Vertx getVertx(){
    return vertx;
  }

  private static void hello(RoutingContext rc) {
    
    JsonObject response = new JsonObject().put("content", "Hello World");

    rc.response()
        .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
        .end(response.encodePrettily());
  }

  private static void health(RoutingContext rc) {
    
    JsonObject response = new JsonObject().put("status", "OK");

    rc.response()
        .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
        .end(response.encodePrettily());
  }
}