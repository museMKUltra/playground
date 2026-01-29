package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roller.playground.entities.Category;
import roller.playground.entities.Product;
import roller.playground.repositories.CategoryRepository;
import roller.playground.repositories.ProductRepository;
import roller.playground.repositories.UserRepository;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void createProductWithCategory(Byte categoryId) {
        var category = categoryRepository.findById(categoryId).orElseThrow();
        var product = Product.builder()
                .name("product2")
                .description("desc")
                .price(BigDecimal.ZERO)
                .build();
        product.setCategory(category);
        productRepository.save(product);
    }

    @Transactional
    public void addAllProductToWishlist(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        productRepository.findAll().forEach(product -> user.addWishlist(product));
        userRepository.save(user);
    }
}
