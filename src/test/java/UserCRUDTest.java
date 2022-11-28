import dev.catalkaya.bimanager.Main;
import dev.catalkaya.bimanager.repository.AuthRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCRUDTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";
    private static final String SESSION_KEY = "test-session";
    private static File dbFile;
    private static Javalin app = null;

    @BeforeAll
    static void beforeAll() throws SQLException {
        dbFile = new File(DATABASE_FILE_PATH);
        if(dbFile.exists()){
            dbFile.delete();
        }
        Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:bimanager.sqlite", null, null).load();
        flyway.migrate();

        AuthRepository.registerSessionId(SESSION_KEY, "admin@istrator.de");

        app = Main.createJavalinApp();
        app.start(9090);
    }

    @AfterAll
    static void afterAll(){
        app.stop();
    }

    @Test
    @DisplayName("Validates creation of a new user")
    void createUser() throws URISyntaxException, IOException, InterruptedException {
        final String reqBody = """
        {
            "person_name": "User",
            "person_email": "user@user.de",
            "person_password": "user123",
            "person_is_administrator": false
        }
        """;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(reqBody))
                .uri(new URI("http://127.0.0.1:9090/person/create"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), HttpStatus.OK.getCode());
        assertEquals(response.body(), "OK");
    }

    @Test
    @DisplayName("Validates reading the users")
    void readUser() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .method("GET", HttpRequest.BodyPublishers.ofString("{}"))
                .uri(new URI("http://127.0.0.1:9090/person/query"))
                .header("X-Auth-Token", SESSION_KEY)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), HttpStatus.OK.getCode());
    }
}
