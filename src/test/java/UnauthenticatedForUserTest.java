import dev.catalkaya.bimanager.Main;
import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.repository.AuthRepository;
import dev.catalkaya.bimanager.repository.PersonRepository;
import io.javalin.Javalin;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnauthenticatedForUserTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";
    private static File dbFile;
    private static Javalin app = null;

    @BeforeAll
    static void beforeAll() throws SQLException, NoSuchAlgorithmException {
        dbFile = new File(DATABASE_FILE_PATH);
        if(dbFile.exists()){
            dbFile.delete();
        }
        Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:bimanager.sqlite", null, null).load();
        flyway.migrate();

        Person p = new Person.Builder()
                .withPersonName("Test User")
                .withPersonEmail("user@user.com")
                .withPersonPassword("user123")
                .withPersonIsAdministrator(false)
                .build();
        PersonRepository.create(p);
        AuthRepository.registerSessionId("test-session", p.getPersonEmail());

        app = Main.createJavalinApp();
        app.start(9090);
    }

    @AfterAll
    static void afterAll(){
        app.stop();
    }

    @DisplayName("Validate unauthenticated request for users requesting administrative controllers ")
    @ParameterizedTest(name = "{index} == > Checking ''{0}''")
    @CsvFileSource(resources = "administrator_paths.csv", delimiter = ';')
    void checkUnauthenticatedRequests(String path, String method) throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.noBody())
                .uri(new URI("http://127.0.0.1:9090" + path))
                .header("X-Auth-Token", "test-session")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 401);
        assertEquals(response.body(), "Unauthorized");
    }
}
