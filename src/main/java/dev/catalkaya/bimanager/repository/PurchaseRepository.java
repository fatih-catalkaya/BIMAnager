package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.model.Product;
import dev.catalkaya.bimanager.model.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PurchaseRepository {

    public static void create(Purchase[] purchaseList) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        con.setAutoCommit(false);
        PreparedStatement ps = con.prepareStatement("INSERT INTO purchase VALUES (?, ?, ?, ?, ?, ?)");

        Arrays.stream(purchaseList).forEach(purchase -> {
            try {
                Optional<Product> op = ProductRepository.getProductById(purchase.getPurchaseProductId());
                if(op.isEmpty()){
                    return;
                }
                Product product = op.get();
                purchase.setPurchaseId(UUID.randomUUID().toString());
                purchase.setPurchaseProductName(product.getProductName());
                purchase.setPurchaseProductPrice(product.getProductPrice());
                    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        for (Purchase purchase : purchaseList) {
            ps.setString(1, purchase.getPurchaseId());
            ps.setString(2, purchase.getPersonId());
            ps.setString(3, purchase.getPurchaseDatetime().format(DateTimeFormatter.ISO_DATE_TIME));
            ps.setString(4, purchase.getPurchaseProductName());
            ps.setDouble(5, purchase.getPurchaseProductPrice());
            ps.setInt(6, purchase.getPurchaseAmount());
            ps.addBatch();
        }

        ps.executeBatch();
        con.commit();
        ps.close();
        con.close();
    }

    public static List<Purchase> query(Purchase purchase) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps;
        if(purchase.getPurchaseAfter() == null){
            ps = con.prepareStatement("SELECT * FROM purchase WHERE person_id = ? ORDER BY datetime(purchase_datetime)");
            ps.setString(1, purchase.getPersonId());
        }
        else{
            ps = con.prepareStatement("SELECT * FROM purchase WHERE person_id = ? AND (datetime(purchase_datetime) > datetime( ? )) ORDER BY datetime(purchase_datetime)");
            ps.setString(1, purchase.getPersonId());
            ps.setString(2, purchase.getPurchaseAfter().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        ResultSet rs = ps.executeQuery();
        List<Purchase> purchaseList = new ArrayList<>();
        while(rs.next()){
            purchaseList.add(new Purchase(
                    rs.getString("purchase_id"),
                    rs.getString("person_id"),
                    null,
                    LocalDateTime.parse(rs.getString("purchase_datetime"), DateTimeFormatter.ISO_DATE_TIME),
                    rs.getString("purchase_product_name"),
                    rs.getDouble("purchase_product_price"),
                    rs.getInt("purchase_amount")
            ));
        }

        rs.close();
        ps.close();
        con.close();

        return purchaseList;
    }

    public static void delete(Purchase purchase) throws SQLException {
        Connection con = Database.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("DELETE FROM purchase WHERE purchase_id = ?");
        ps.setString(1, purchase.getPurchaseId());

        ps.executeUpdate();
        con.close();
    }
}
