import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.model.Product;
import dev.catalkaya.bimanager.model.Purchase;
import dev.catalkaya.bimanager.repository.PersonRepository;
import dev.catalkaya.bimanager.repository.ProductRepository;
import dev.catalkaya.bimanager.repository.PurchaseRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseCRUDTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";
    private static final Person PURCHASE_PERSON = new Person.Builder()
            .withPersonEmail("user@user.de")
            .withPersonName("User")
            .withPersonPassword("user123")
            .withPersonIsAdministrator(true)
            .build();
    private static final Product PURCHASE_PRODUCT = new Product.Builder()
            .withProductName("Club Mate")
            .withProductPrice(1.0)
            .build();

    @BeforeAll
    static void beforeAll() throws SQLException, NoSuchAlgorithmException {
        File dbFile = new File(DATABASE_FILE_PATH);
        if(dbFile.exists()){
            dbFile.delete();
        }
        Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:bimanager.sqlite", null, null).load();
        flyway.migrate();

        PersonRepository.create(PURCHASE_PERSON);
        ProductRepository.create(PURCHASE_PRODUCT);
    }

    @Test
    @DisplayName("Validating creation of purchase")
    void createPurchase() throws SQLException {
        Purchase p = new Purchase.Builder()
                .withPurchaseAmount(3)
                .withPurchaseProductId(PURCHASE_PRODUCT.getProductId())
                .withPersonId(PURCHASE_PERSON.getPersonId())
                .withPurchaseDatetime(LocalDateTime.now())
                .build();
        PurchaseRepository.create(new Purchase[]{p});
        assertFalse(p.getPurchaseId().isEmpty());
    }

    @Test
    @DisplayName("Validate querying of purchases")
    void queryPurchase() throws SQLException {
        final LocalDateTime buyTime = LocalDateTime.of(1,1,1,12,0);

        Purchase p = new Purchase.Builder()
                .withPurchaseAmount(22)
                .withPurchaseProductId(PURCHASE_PRODUCT.getProductId())
                .withPersonId(PURCHASE_PERSON.getPersonId())
                .withPurchaseDatetime(buyTime)
                .build();
        PurchaseRepository.create(new Purchase[]{p});

        assertTrue(
                PurchaseRepository.query(new Purchase.Builder().withPersonId(PURCHASE_PERSON.getPersonId()).build())
                    .stream()
                    .anyMatch(product -> product.getPurchaseAmount() == 22)
        );
        assertTrue(
            PurchaseRepository.query(new Purchase.Builder()
                            .withPersonId(PURCHASE_PERSON.getPersonId())
                            .withPurchaseDatetime(buyTime.minus(3, ChronoUnit.HOURS))
                            .build())
                    .stream()
                    .anyMatch(product -> product.getPurchaseAmount() == 22)
        );
        assertFalse(
                PurchaseRepository.query(new Purchase.Builder()
                                .withPersonId(PURCHASE_PERSON.getPersonId())
                                .withPurchaseAfter(buyTime.plus(3, ChronoUnit.HOURS))
                                .build())
                        .stream()
                        .anyMatch(product -> product.getPurchaseAmount() == 22)
        );
    }

    @Test
    @DisplayName("Validate deletion of purchases")
    void deletePurchase() throws SQLException{
        Purchase p = new Purchase.Builder()
                .withPurchaseAmount(1337)
                .withPurchaseProductId(PURCHASE_PRODUCT.getProductId())
                .withPersonId(PURCHASE_PERSON.getPersonId())
                .withPurchaseDatetime(LocalDateTime.now())
                .build();
        PurchaseRepository.create(new Purchase[]{p});
        PurchaseRepository.delete(p);
        assertFalse(
                PurchaseRepository.query(new Purchase.Builder().withPersonId(PURCHASE_PERSON.getPersonId()).build())
                        .stream()
                        .anyMatch(product -> product.getPurchaseAmount() == 1337)
        );
    }
}
