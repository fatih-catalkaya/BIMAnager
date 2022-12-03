import dev.catalkaya.bimanager.model.Deposit;
import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.repository.DepositRepository;
import dev.catalkaya.bimanager.repository.PersonRepository;
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

public class DepositCRUDTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";
    private static final Person PURCHASE_PERSON = new Person.Builder()
            .withPersonEmail("user@user.de")
            .withPersonName("User")
            .withPersonPassword("user123")
            .withPersonIsAdministrator(true)
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
    }

    @Test
    @DisplayName("Validate creation of deposit")
    void createDeposit() throws SQLException {
        Deposit d = new Deposit.Builder()
                .withPersonId(PURCHASE_PERSON.getPersonId())
                .withDepositAmount(1.0)
                .withDepositDatetime(LocalDateTime.now())
                .withDepositMethod("Cash")
                .build();
        DepositRepository.create(d);
        assertFalse(d.getDepositId().isEmpty());
    }

    @Test
    @DisplayName("Validate querying of deposit")
    void queryDeposit() throws SQLException {

        LocalDateTime creationDatetime = LocalDateTime.of(2000,1,1, 1,1,1);

        Deposit d = new Deposit.Builder()
                .withPersonId(PURCHASE_PERSON.getPersonId())
                .withDepositAmount(10.0)
                .withDepositDatetime(creationDatetime)
                .withDepositMethod("Cash")
                .build();
        DepositRepository.create(d);

        assertTrue(
                DepositRepository.query(new Deposit.Builder()
                                .withPersonId(PURCHASE_PERSON.getPersonId())
                                .build())
                .stream()
                .anyMatch(deposit -> deposit.getDepositAmount() == 10.0)
        );

        assertTrue(
                DepositRepository.query(new Deposit.Builder()
                                .withPersonId(PURCHASE_PERSON.getPersonId())
                                .withDepositAfter(creationDatetime.minus(10, ChronoUnit.HOURS))
                                .build())
                        .stream()
                        .anyMatch(deposit -> deposit.getDepositAmount() == 10.0)
        );

        assertTrue(
                DepositRepository.query(new Deposit.Builder()
                                .withPersonId(PURCHASE_PERSON.getPersonId())
                                .withDepositAfter(creationDatetime.plus(10, ChronoUnit.HOURS))
                                .build())
                        .stream()
                        .noneMatch(deposit -> deposit.getDepositAmount() == 10.0)
        );
    }

    @Test
    @DisplayName("Validate querying of deposit")
    void deleteDeposit() throws SQLException {
        Deposit d = new Deposit.Builder()
                .withPersonId(PURCHASE_PERSON.getPersonId())
                .withDepositAmount(20.0)
                .withDepositDatetime(LocalDateTime.now())
                .withDepositMethod("Cash")
                .build();
        DepositRepository.create(d);
        DepositRepository.delete(d);

        assertTrue(
                DepositRepository.query(new Deposit.Builder()
                                .withPersonId(PURCHASE_PERSON.getPersonId())
                                .build())
                        .stream()
                        .noneMatch(deposit -> deposit.getDepositAmount() == 20.0)
        );
    }
}
