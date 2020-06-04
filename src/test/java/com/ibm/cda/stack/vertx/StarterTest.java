package com.ibm.cda.stack.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class StarterTest {

    public static final int PORT = 8080;
    //private Vertx vertx;
    private Starter starter;
    private WebClient client;

    @Before
    public void before(TestContext context) {
        starter = new Starter();
        starter.launch();
        Vertx vertx = starter.getVertx();
        vertx.exceptionHandler(context.exceptionHandler());
        //vertx.deployVerticle(Starter.class.getName(),
        //    new DeploymentOptions().setConfig(new JsonObject().put("http.port", PORT)),
        //    context.asyncAssertSuccess());
        
        client = WebClient.create(vertx);
    }

    @After
    public void after(TestContext context) {
        starter.getVertx().close(context.asyncAssertSuccess());
    }

    @Test
    public void callGreetingTest(TestContext context) {
        // Send a request and get a response
        Async async = context.async();
        client.get(PORT, "localhost", "/hello")
            .send(resp -> {
                context.assertTrue(resp.succeeded());
                context.assertEquals(resp.result().statusCode(), 200);
                String content = resp.result().bodyAsJsonObject().getString("content");
                context.assertEquals(content, "Hello World");
                async.complete();
            });
    }

}