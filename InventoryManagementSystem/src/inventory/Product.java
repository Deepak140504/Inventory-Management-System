package inventory;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int quantity;
    private double price;

    public Product(int id, String name, int quantity, double price) {

        if (quantity < 0) quantity = 0;
        if (price < 0) price = 0;

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalValue() {
        return quantity * price;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0)
            this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(
                "ID : %d%nName : %s%nQuantity : %d%nPrice : %.2f%n----------------------",
                id, name, quantity, price
        );
    }
}