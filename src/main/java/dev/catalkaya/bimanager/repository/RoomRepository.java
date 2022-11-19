package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public static void delete(Room room) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM room WHERE room_name = ?");
        ps.setString(1, room.getRoomName());
        ps.executeUpdate();
        con.close();
    }

    public static List<Room> findAll() throws SQLException {
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM room ORDER BY room_name");
        ResultSet rs = ps.executeQuery();
        List<Room> resultList = new ArrayList<>();
        while(rs.next()){
            Room r = new Room(rs.getString("room_id"), rs.getString("room_name"));
            resultList.add(r);
        }
        rs.close();
        ps.close();
        return resultList;
    }
}
