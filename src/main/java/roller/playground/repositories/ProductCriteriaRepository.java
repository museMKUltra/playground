package roller.playground.repositories;

import roller.playground.entities.Category;
import roller.playground.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findProductsByCategory(Category category);
}
