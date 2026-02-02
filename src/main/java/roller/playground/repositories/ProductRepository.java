package roller.playground.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import roller.playground.dtos.ProductSummaryDTO;
import roller.playground.entities.Category;
import roller.playground.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCriteriaRepository {
    @Procedure("findProductsByPrice")
    List<Product> findProducts(BigDecimal min, BigDecimal max);

    @Query("select count(p) from Product p where p.price between :min and :max")
    Long countProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Modifying
    @Query("update Product p set p.price = :newPrice where p.category.id = :categoryId")
    void updatePriceByCategoryId(@Param("newPrice") BigDecimal newPrice, @Param("categoryId") Byte categoryId);

    @Query("select new roller.playground.dtos.ProductSummaryDTO(p.id, p.name) from Product p where p.category = :category")
    List<ProductSummaryDTO> findByCategory(@Param("category") Category category);
}
