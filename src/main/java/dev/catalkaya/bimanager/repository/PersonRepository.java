package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.Utils;
import dev.catalkaya.bimanager.model.Person;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class PersonRepository {

    public static Person create(Person person) throws SQLException, NoSuchAlgorithmException {
        final UUID uuid = UUID.randomUUID();
        Connection con = Database.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("INSERT INTO person VALUES (?,?,?,?,?,?)");
        ps.setString(1, uuid.toString());
        ps.setString(2, person.getPersonName());
        ps.setString(3, person.getPersonEmail());
        ps.setString(4, Utils.hashSHA3(person.getPersonPassword()));
        ps.setString(5, person.getPersonRoomName());
        ps.setInt(6, (person.isPersonIsAdministrator() ? 1 : 0));

        ps.executeUpdate();
        con.close();
        person.setPersonId(uuid.toString());
        return person;
    }

    public static void delete(Person person) throws SQLException {
        Connection con = Database.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("DELETE FROM person WHERE person_id = ?");
        ps.setString(1, person.getPersonId());

        ps.executeUpdate();
        con.close();
    }
}
