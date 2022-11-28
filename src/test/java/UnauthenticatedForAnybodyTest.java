import dev.catalkaya.bimanager.Main;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnauthenticatedForAnybodyTest {
    private static final String DATABASE_FILE_PATH = "bimanager.sqlite";
    private static File dbFile;
    private static Javalin app = null;

    @BeforeAll
    static void beforeAll(){
        dbFile = new File(DATABASE_FILE_PATH);
        if(dbFile.exists()){
            dbFile.delete();
        }
        Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:bimanager.sqlite", null, null).load();
        flyway.migrate();

        app = Main.createJavalinApp();
        app.start(9090);
    }

    @AfterAll
    static void afterAll(){
        app.stop();
    }


    @DisplayName("Check if unauthenticated requests return 401-Unauthenticated responses ")
    @ParameterizedTest(name = "{index} == > Checking ''{0}''")
    @CsvFileSource(resources = "paths_methods.csv", delimiter = ';', numLinesToSkip = 2)
    void checkUnauthenticatedRequests(String path, String method) throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .method(method, HttpRequest.BodyPublishers.noBody())
                .uri(new URI("http://127.0.0.1:9090" + path))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), HttpStatus.UNAUTHORIZED.getCode());
        assertEquals(response.body(), "Unauthorized");
    }
}
