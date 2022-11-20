package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.Utils;
import dev.catalkaya.bimanager.model.Person;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    public static List<Person> query(Person person) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        StringBuilder sb = new StringBuilder("SELECT * FROM person ");
        String val = null;

        if(person.getPersonId() != null && !person.getPersonId().isEmpty()){
            sb.append( " WHERE person_id = ?");
            val = person.getPersonId();
        }
        else if(person.getPersonName() != null && !person.getPersonName().isEmpty()){
            sb.append( " WHERE person_name = ?");
            val = person.getPersonName();
        }
        else if(person.getPersonEmail() != null && !person.getPersonEmail().isEmpty()){
            sb.append( " WHERE person_email = ?");
            val = person.getPersonEmail();
        }

        PreparedStatement ps = con.prepareStatement(sb.toString());
        if(val != null){
            ps.setString(1, val);
        }

        ResultSet rs = ps.executeQuery();
        List<Person> resultList = new ArrayList<>();
        while(rs.next()){
            Person p = new Person.Builder()
                    .withPersonId(rs.getString("person_id"))
                    .withPersonName(rs.getString("person_name"))
                    .withPersonEmail(rs.getString("person_email"))
                    .withPersonRoomName(rs.getString("person_room_name"))
                    .withPersonIsAdministrator((rs.getInt("person_is_administrator") == 1))
                    .build();
            resultList.add(p);
        }

        rs.close();
        ps.close();

        return resultList;
    }

    public static void update(Person person) throws SQLException, NoSuchAlgorithmException {
        Connection con = Database.getInstance().getConnection();
        Map<String, Object> keyVal = new HashMap<>();

        if(person.getPersonName() != null && !person.getPersonName().isEmpty()){
            keyVal.put("person_name", person.getPersonName());
        }
        if(person.getPersonEmail() != null && !person.getPersonEmail().isEmpty()){
            keyVal.put("person_email", person.getPersonEmail());
        }
        if(person.getPersonPassword() != null && !person.getPersonPassword().isEmpty()){
            keyVal.put("person_password", person.getPersonPassword());
        }
        if(person.getPersonRoomName() != null && !person.getPersonRoomName().isEmpty()){
            keyVal.put("person_room_name", person.getPersonRoomName());
        }

        StringBuilder sb = new StringBuilder("UPDATE person SET ");
        for (Map.Entry<String, Object> entry : keyVal.entrySet()){
            sb.append(" ").append(entry.getKey()).append(" = ?,");
        }
        sb.append(" person_is_administrator = ? WHERE person_id = ?");

        int i = 1;
        PreparedStatement ps = con.prepareStatement(sb.toString());

        for (Map.Entry<String, Object> entry : keyVal.entrySet()){
            if(entry.getKey().equals("person_password")){
                ps.setString(i++, Utils.hashSHA3(person.getPersonPassword()));
            }
            else{
                ps.setString(i++, (String)entry.getValue());
            }
        }
        ps.setInt(i++, (person.isPersonIsAdministrator() ? 1 : 0));
        ps.setString(i++, person.getPersonId());

        ps.executeUpdate();
        con.close();
    }

    public static void delete(Person person) throws SQLException {
        Connection con = Database.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("DELETE FROM person WHERE person_id = ?");
        ps.setString(1, person.getPersonId());

        ps.executeUpdate();
        con.close();
    }
}
