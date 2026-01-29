package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import roller.playground.entities.Category;
import roller.playground.entities.Product;
import roller.playground.repositories.ProductRepository;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct() {
        var category = Category.builder()
                .name("cat 1")
                .build();
        var product = Product.builder()
                .name("product")
                .description("desc")
                .price(BigDecimal.ZERO)
                .build();
        product.setCategory(category);
        productRepository.save(product);
    }
}
