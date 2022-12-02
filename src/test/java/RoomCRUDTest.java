import dev.catalkaya.bimanager.model.Room;
import dev.catalkaya.bimanager.repository.RoomRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class RoomCRUDTest {
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
    @DisplayName("Validate creation of room")
    void createRoom() throws SQLException {
        Room r = new Room.Builder().withRoomName("Test Room").build();
        RoomRepository.create(r);
        assertTrue(StringUtils.isNotBlank(r.getRoomId()));
    }

    @Test
    @DisplayName("Validate deletion of room")
    void deleteRoom() throws SQLException {
        Room r = new Room.Builder().withRoomName("Deleted Room").build();
        RoomRepository.create(r);
        RoomRepository.delete(r);
        assertTrue(RoomRepository.findAll()
                    .stream()
                    .filter(room -> room.getRoomName().equalsIgnoreCase("Deleted Room"))
                    .findFirst()
                    .isEmpty());
    }

    @Test
    @DisplayName("Validate query of rooms")
    void queryRoom() throws SQLException {
        IntStream.range(0, 10).parallel().forEach(i -> {
            try{
                Room r = new Room.Builder().withRoomName("Room %d".formatted(i)).build();
                RoomRepository.create(r);
            }
            catch (Exception ex){
                ex.printStackTrace();
                fail();
            }
        });
        assertEquals(RoomRepository.findAll()
                .stream()
                .filter(room -> room.getRoomName().matches("Room \\d"))
                .count(),
                10
        );
    }
}