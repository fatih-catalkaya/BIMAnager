import dev.catalkaya.bimanager.model.Product;
import dev.catalkaya.bimanager.repository.ProductRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCRUDTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";

    @BeforeAll
    static void beforeAll() throws SQLException {
        File dbFile = new File(DATABASE_FILE_PATH);
        if(dbFile.exists()){
            dbFile.delete();
        }
        Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:bimanager.sqlite", null, null).load();
        flyway.migrate();
    }

    @Test
    @DisplayName("Validate creation of products")
    void createProduct() throws SQLException {
        Product p = new Product.Builder()
                .withProductName("Coke")
                .withProductPrice(1.0)
                .build();
        ProductRepository.create(p);
        assertTrue(StringUtils.isNotBlank(p.getProductId()));
    }

    @Test
    @DisplayName("Validate querying of products")
    void queryProduct() throws SQLException {
        Product p = new Product.Builder()
                .withProductName("Lemonade")
                .withProductPrice(1.0)
                .build();
        ProductRepository.create(p);

        assertFalse(ProductRepository.query(new Product.Builder().build()).isEmpty());
        assertFalse(ProductRepository.query(new Product.Builder().withProductId(p.getProductId()).build()).isEmpty());
        assertFalse(ProductRepository.query(new Product.Builder().withProductName(p.getProductName()).build()).isEmpty());
    }

    @Test
    @DisplayName("Validate update of product")
    void updateProduct() throws SQLException {
        Product p = new Product.Builder()
                .withProductName("Orange juice")
                .withProductPrice(1.0)
                .build();
        ProductRepository.create(p);
        p.setProductPrice(2.0);
        ProductRepository.update(p);
        assertEquals(
                2.0,
                ProductRepository.query(new Product.Builder().build()).stream()
                    .filter(product -> product.getProductName().equalsIgnoreCase("Orange juice"))
                    .findFirst()
                    .get()
                    .getProductPrice()
        );
    }

    @Test
    @DisplayName("Validate deletion of products")
    void deleteProduct() throws SQLException {
        Product p = new Product.Builder()
                .withProductName("Water")
                .withProductPrice(1.0)
                .build();
        ProductRepository.create(p);
        ProductRepository.delete(p);
        assertTrue(ProductRepository.query(new Product.Builder().withProductId(p.getProductId()).build()).isEmpty());
    }
}
