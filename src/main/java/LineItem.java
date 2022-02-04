import java.math.BigDecimal;

public record LineItem(Product product, int quantity) {
    public BigDecimal getTotal() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}
