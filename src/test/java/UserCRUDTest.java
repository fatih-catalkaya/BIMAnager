import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.repository.PersonRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserCRUDTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";
    private static final String SESSION_KEY = "test-session";

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
    @DisplayName("Validates creation of a new user")
    void createUser() throws SQLException, NoSuchAlgorithmException {
        Person p = new Person.Builder()
                .withPersonName("User")
                .withPersonEmail("user@user.com")
                .withPersonPassword("user123")
                .withPersonIsAdministrator(false)
                .build();
        PersonRepository.create(p);

        assertTrue(StringUtils.isNotBlank(p.getPersonId()));
    }

    @Test
    @DisplayName("Validates reading the users")
    void readUser() throws SQLException {
        // By default, administrator is created
        List<Person> personList = PersonRepository.query(new Person.Builder().build());
        assertFalse(personList.isEmpty());

        personList = PersonRepository.query(new Person.Builder().withPersonEmail("admin@istrator.de").build());
        assertFalse(personList.isEmpty());

        personList = PersonRepository.query(new Person.Builder()
                .withPersonId("c893a566-4470-4e94-a6f3-607c16753c3a")
                .build());
        assertFalse(personList.isEmpty());
    }

    @Test
    @DisplayName("Validate update of a user")
    void updateUser() throws SQLException, NoSuchAlgorithmException {
        Person admin = new Person.Builder()
                .withPersonId("c893a566-4470-4e94-a6f3-607c16753c3a")
                .withPersonEmail("admin@istrator.com")
                .build();
        PersonRepository.update(admin);

        List<Person> personList = PersonRepository.query(new Person.Builder()
                .withPersonEmail("admin@istrator.com")
                .build());
        assertFalse(personList.isEmpty());
    }

    @Test
    @DisplayName("Validate deletion of a user")
    void deleteUser() throws SQLException {
        Person admin = new Person.Builder().withPersonId("c893a566-4470-4e94-a6f3-607c16753c3a").build();
        PersonRepository.delete(admin);
        List<Person> personList = PersonRepository.query(admin);
        assertTrue(personList.isEmpty());
    }
}
