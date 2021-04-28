package demo;

import java.math.BigDecimal;

public class DeliveryResponse {

    private String city;
    private int items;
    private BigDecimal price;

    public DeliveryResponse() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
