package demo;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Path("/")
public class DeliveryQuoteEndpoint {

    @POST
    public Uni<DeliveryResponse> request(DeliveryRequest request) {
        return Uni.createFrom().item(() -> {
            var perItem = new BigDecimal("3.92").add(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0.40d)));
            var count = BigDecimal.valueOf(request.getItems());

            var response = new DeliveryResponse();
            response.setCity(request.getCity());
            response.setItems(request.getItems());
            response.setPrice(perItem.multiply(count));

            return response;
        }).onItem().delayIt().by(Duration.ofMillis(ThreadLocalRandom.current().nextLong(1000L)));
    }
}
