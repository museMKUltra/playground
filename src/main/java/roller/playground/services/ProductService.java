package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roller.playground.entities.Category;
import roller.playground.entities.Product;
import roller.playground.repositories.CategoryRepository;
import roller.playground.repositories.ProductRepository;
import roller.playground.repositories.UserRepository;
import roller.playground.repositories.specifications.ProductSpec;

import java.math.BigDecimal;
import java.util.List;

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

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void updateProductPrice() {
        productRepository.updatePriceByCategoryId(BigDecimal.valueOf(10), (byte) 1);
    }

    public void fetchProducts() {
        var products = productRepository.findByCategory(new Category((byte) 1));
        System.out.println(products);
    }

    @Transactional
    public void fetchProductsBetweenPrices() {
        var products = productRepository.findProducts(BigDecimal.valueOf(10), BigDecimal.valueOf(20));
        System.out.println(products);
    }

    public void fetchProductsByExample() {
        var product = Product.builder()
                .name("product")
                .build();
        var matcher = ExampleMatcher.matching()
                .withIncludeNullValues()
                .withIgnorePaths("id", "description")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        var example = Example.of(product, matcher);
        var products = productRepository.findAll(example);
        System.out.println(products);
    }

    public void fetchProductsByCriteria() {
        var products = productRepository.findProductsByCriteria("prod", BigDecimal.valueOf(1), null);
        products.forEach(System.out::println);
    }

    public void fetchProductsBySpecifications(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        Specification<Product> spec = Specification.allOf();

        if (name != null) {
            spec = spec.and(ProductSpec.hasName(name));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpec.hasPriceGreaterThanOrEqualTo(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpec.hasPriceLessThanOrEqualTo(maxPrice));
        }

        productRepository.findAll(spec).forEach(System.out::println);
    }

    public void fetchSortedProducts() {
        var sort = Sort.by("name").and(
                Sort.by("price").descending()
        );

        productRepository.findAll(sort).forEach(System.out::println);
    }

    public void fetchPaginatedProducts(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Product> page = productRepository.findAll(pageRequest);

        var products = page.getContent();
        products.forEach(System.out::println);

        var totalPages = page.getTotalPages();
        var totalElements = page.getTotalElements();
        System.out.println("Total Pages:" + totalPages);
        System.out.println("Total Elements:" + totalElements);
    }
}
