package hw11;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** One type of snack and its current inventory. */
public final class Snack {
    private final String name;
    private final BigDecimal price;
    private int quantity;

    public Snack(String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A snack must have a name.");
        }
        if (!Double.isFinite(price) || price <= 0 || quantity < 0) {
            throw new IllegalArgumentException("Price must be positive and quantity cannot be negative.");
        }
        this.name = name.trim();
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.UNNECESSARY);
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    void dispenseOne() {
        if (quantity == 0) {
            throw new IllegalStateException(name + " is sold out.");
        }
        quantity--;
    }
}
