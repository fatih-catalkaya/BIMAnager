package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.Utils;
import dev.catalkaya.bimanager.model.Person;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

public class AuthRepository {
    public static Optional<Person> getPersonBySessionId(final String sessionId) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT person.* FROM session LEFT JOIN person ON session.session_person_id = person.person_id WHERE session_auth_key = ?"
        );
        ps.setString(1, sessionId);
        ResultSet rs = ps.executeQuery();
        Optional<Person> result;
        if(rs.next()){
            result = Optional.of(new Person(
                    rs.getString("person_id"),
                    rs.getString("person_name"),
                    rs.getString("person_email"),
                    null,
                    rs.getString("person_room_name"),
                    (rs.getInt("person_is_administrator") == 1)
            ));
        }
        else{
            result = Optional.empty();
        }

        rs.close();
        ps.close();
        con.close();
        return result;
    }

    public static boolean isValid(Person person) throws SQLException, NoSuchAlgorithmException {
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(person_id) AS num_rows FROM person WHERE person_email = ? AND person_password = ?"
        );
        ps.setString(1, person.getPersonEmail());
        ps.setString(2, Utils.hashSHA3(person.getPersonPassword()));

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt("num_rows");

        rs.close();
        ps.close();
        con.close();
        return (count == 1);
    }

    public static void deleteSessionId(final String sessionId) throws SQLException{
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM session WHERE session_id = ?"
        );
        ps.setString(1, sessionId);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public static void registerSessionId(final String sessionId, final String personEmail) throws SQLException{
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT person_id FROM person WHERE person_email = ?"
        );
        ps.setString(1, personEmail);
        ResultSet rs = ps.executeQuery();
        rs.next();
        final String personId = rs.getString("person_id");
        rs.close();
        ps.close();

        ps = con.prepareStatement("INSERT INTO session(session_id, session_auth_key, session_person_id, session_start_datetime) VALUES (?, ?, ?, ?)");
        ps.setString(1, UUID.randomUUID().toString());
        ps.setString(2, sessionId);
        ps.setString(3, personId);
        ps.setString(4, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        ps.executeUpdate();

        ps.close();
        con.close();
    }
}
