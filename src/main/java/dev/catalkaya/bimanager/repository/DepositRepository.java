package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.model.Deposit;
import dev.catalkaya.bimanager.model.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DepositRepository {
    public static Deposit create(Deposit deposit) throws SQLException {
        final String uuid = UUID.randomUUID().toString();
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO deposit(deposit_id, person_id, deposit_datetime, deposit_amount, deposit_method) VALUES (?, ?, ?, ?, ?)");

        ps.setString(1, uuid);
        ps.setString(2, deposit.getPersonId());
        ps.setString(3, deposit.getDepositDatetime().format(DateTimeFormatter.ISO_DATE_TIME));
        ps.setDouble(4, deposit.getDepositAmount());
        ps.setString(5, deposit.getDepositMethod());
        ps.executeUpdate();

        deposit.setDepositId(uuid);

        ps.close();
        con.close();

        return deposit;
    }

    public static Deposit delete(Deposit deposit) throws SQLException{
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM deposit WHERE deposit_id = ?");

        ps.setString(1, deposit.getDepositId());
        ps.executeUpdate();

        ps.close();
        con.close();

        return deposit;
    }

    public static List<Deposit> query(Deposit deposit) throws SQLException{
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps;
        if(deposit.getDepositAfter() == null){
            ps = con.prepareStatement("SELECT * FROM deposit WHERE person_id = ? ORDER BY datetime(deposit_datetime)");
            ps.setString(1, deposit.getPersonId());
        }
        else{
            ps = con.prepareStatement("SELECT * FROM deposit WHERE person_id = ? AND (datetime(deposit_datetime) > datetime( ? )) ORDER BY datetime(deposit_datetime)");
            ps.setString(1, deposit.getPersonId());
            ps.setString(2, deposit.getDepositAfter().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        ResultSet rs = ps.executeQuery();
        List<Deposit> depositList = new ArrayList<>();
        while(rs.next()){
            depositList.add(new Deposit(
                    rs.getString("deposit_id"),
                    rs.getString("person_id"),
                    LocalDateTime.parse(rs.getString("deposit_datetime"), DateTimeFormatter.ISO_DATE_TIME),
                    rs.getDouble("deposit_amount"),
                    rs.getString("deposit_method"),
                    null
            ));
        }

        rs.close();
        ps.close();
        con.close();

        return depositList;
    }
}
