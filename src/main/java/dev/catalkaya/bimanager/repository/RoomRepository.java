package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class RoomRepository {
    public static Room create(Room room) throws SQLException {
        final UUID uuid = UUID.randomUUID();
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO room VALUES (?, ?)");
        ps.setString(1, uuid.toString());
        ps.setString(2, room.getRoomName());
        ps.executeUpdate();
        con.close();
        room.setRoomId(uuid.toString());
        return room;
    }
}
