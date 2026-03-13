package roller.playground.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@Table(name = "orders", schema = "playground")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<OrderItem> orderItems = new HashSet<>();

    public static Order fromCart(Cart cart, User customer) {
        var order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .totalPrice(cart.getTotalPrice())
                .build();

        cart.getItems().forEach(item -> {
            var product = item.getProduct();
            order.addOrderItem(product, item.getQuantity(), product.getPrice(), item.getTotalPrice());
        });

        return order;
    }

    public void addOrderItem(Product product, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        var orderItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public BigDecimal getTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isPlacedBy(User user) {
        return this.customer.getId().equals(user.getId());
    }
}