package demo;

import java.util.Objects;

public class DeliveryRequest {

    private String city;
    private int items;

    public DeliveryRequest() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        Objects.requireNonNull(city, "The city cannot be null");
        this.city = city;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        if (items <= 0) {
            throw new IllegalArgumentException("The items count must be strictly positive");
        }
        this.items = items;
    }
}
