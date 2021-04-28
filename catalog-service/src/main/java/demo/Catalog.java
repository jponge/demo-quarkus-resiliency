package demo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("/")
public class Catalog {

    @POST
    @Transactional
    public Uni<Product> persist(Product product) {
        return product
                .persistAndFlush().replaceWith(product)
                .onFailure(err -> err.getMessage().contains("duplicate key value violates unique constraint")).recoverWithNull();
    }

    @GET
    public Multi<Product> getAll() {
        return Product.all();
    }

    @GET
    @Path("/id/{id}")
    public Uni<Product> getById(Long id) {
        return Product.findById(id);
    }

    @GET
    @Path("/named/{name}")
    public Uni<Product> getByName(String name) {
        return Product.find("name", name).firstResult();
    }
}
