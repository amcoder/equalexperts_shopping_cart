import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Cart {
    private final BigDecimal taxRate;
    private final Map<String, LineItem> lineItems = new HashMap<>();

    public Cart(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
    public void add(Product product, int quantity) {
        lineItems.merge(product.id(),
                new LineItem(product, quantity),
                (oldItem, newItem) -> new LineItem(product, oldItem.quantity() + quantity));
    }

    public Collection<LineItem> getLineItems() {
        return List.copyOf(lineItems.values());
    }

    public BigDecimal getSubTotal() {
        return lineItems.values().stream()
                .map(LineItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSalesTax() {
        return getSubTotal()
                .multiply(taxRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return getSubTotal()
                .add(getSalesTax());
    }
}
