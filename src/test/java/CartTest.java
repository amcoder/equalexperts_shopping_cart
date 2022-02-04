import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {
    @Test
    void emptyCart() {
        var cart = new Cart(BigDecimal.ZERO);

        assertThat(cart.getLineItems())
                .isEmpty();
        assertThat(cart.getSubTotal())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(0));
        assertThat(cart.getSalesTax())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(0));
        assertThat(cart.getTotal())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(0));
    }

    @Test
    void addSingleProduct() {
        var cart = new Cart(BigDecimal.ZERO);
        var dove = new Product("dove_soap", "Dove Soap", BigDecimal.valueOf(39.99));

        cart.add(dove, 1);

        assertThat(cart.getLineItems())
                .hasSize(1)
                .anySatisfy(i -> {
                    assertThat(i.product().name()).isEqualTo(dove.name());
                    assertThat(i.product().price()).isEqualTo(dove.price());
                    assertThat(i.quantity()).isEqualTo(1);
                });
        assertThat(cart.getTotal())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(39.99));
    }

    @Test
    void addManyProducts() {
        var cart = new Cart(BigDecimal.ZERO);
        var dove = new Product("dove_soap", "Dove Soap", BigDecimal.valueOf(39.99));

        cart.add(dove, 5);
        cart.add(dove, 3);

        assertThat(cart.getLineItems())
                .hasSize(1)
                .anySatisfy(i -> {
                    assertThat(i.product().name()).isEqualTo(dove.name());
                    assertThat(i.product().price()).isEqualTo(dove.price());
                    assertThat(i.quantity()).isEqualTo(8);
                });
        assertThat(cart.getTotal())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(319.92));
    }

    @Test
    void calculateTaxRateWithManyProducts() {
        var cart = new Cart(BigDecimal.valueOf(.125));
        var dove = new Product("dove_soap", "Dove Soap", BigDecimal.valueOf(39.99));
        var axe = new Product("axe_deo", "Axe Deo", BigDecimal.valueOf(99.99));

        cart.add(dove, 2);
        cart.add(axe, 2);

        assertThat(cart.getLineItems())
                .hasSize(2)
                .anySatisfy(i -> {
                    assertThat(i.product().name()).isEqualTo(dove.name());
                    assertThat(i.product().price()).isEqualTo(dove.price());
                    assertThat(i.quantity()).isEqualTo(2);
                })
                .anySatisfy(i -> {
                    assertThat(i.product().name()).isEqualTo(axe.name());
                    assertThat(i.product().price()).isEqualTo(axe.price());
                    assertThat(i.quantity()).isEqualTo(2);
                });
        assertThat(cart.getSubTotal())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(279.96));
        assertThat(cart.getSalesTax())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(35));
        assertThat(cart.getTotal())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(314.96));
    }
}