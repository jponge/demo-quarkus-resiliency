package demo;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Multi;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Product extends PanacheEntity {

    @Column(unique = true)
    public String name;

    public BigDecimal price;

    public static Multi<Product> all() {
        return findAll().stream();
    }
}
