package demo;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.stream.Collectors;

@Path("/")
public class Api {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "catalog.hostname")
    String catalogHostname;

    @ConfigProperty(name = "catalog.port")
    int catalogPort;

    @ConfigProperty(name = "delivery.hostname")
    String deliveryHostname;

    @ConfigProperty(name = "delivery.port")
    int deliveryPort;

    WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.create(vertx);
    }

    @POST
    public Uni<JsonObject> processRequest(JsonObject json) {

        var city = json.getString("city", "N/A");
        var products = json.getJsonArray("products");

        var productCount = products.stream()
                .map(JsonObject.class::cast)
                .mapToInt(entry -> entry.getInteger("count"))
                .sum();

        var productRequests = products.stream()
                .map(JsonObject.class::cast)
                .map(this::catalogRequest)
                .collect(Collectors.toList());

        return Uni.combine()
                .all().unis(productRequests).combinedWith(JsonArray::new)
                .onItem().transformToUni(array -> deliveryRequest(city, productCount, array));
    }

    private Uni<JsonObject> deliveryRequest(String city, int productCount, JsonArray array) {
        var deliveryRequest = new JsonObject()
                .put("city", city)
                .put("items", productCount);

        return webClient.post(deliveryPort, deliveryHostname, "/")
                .as(BodyCodec.jsonObject())
                .sendJson(deliveryRequest)
                .onItem().transform(response -> {
                    JsonObject body = response.body();
                    body.put("deliveryPrice", body.getValue("price"));
                    body.remove("price");
                    return body.mergeIn(new JsonObject().put("references", array));
                });
    }

    private Uni<JsonObject> catalogRequest(JsonObject pjson) {
        return webClient.get(catalogPort, catalogHostname, "/named/" + pjson.getString("name"))
                .as(BodyCodec.jsonObject())
                .send()
                .onItem().transform(response -> response.body().put("count", pjson.getInteger("count")));
    }
}
