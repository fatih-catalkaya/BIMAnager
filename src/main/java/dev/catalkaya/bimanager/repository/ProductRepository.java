package dev.catalkaya.bimanager.repository;

import dev.catalkaya.bimanager.Database;
import dev.catalkaya.bimanager.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductRepository {

    public static Product create(Product product) throws SQLException {
        final UUID uuid = UUID.randomUUID();
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO product VALUES (?,?,?)");

        ps.setString(1, uuid.toString());
        ps.setString(2, product.getProductName());
        ps.setDouble(3, product.getProductPrice());

        ps.executeUpdate();
        con.close();
        product.setProductId(uuid.toString());
        return product;
    }

    public static List<Product> query(Product product) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM product");
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps;
        if(product.getProductId() != null && !product.getProductName().isEmpty()){
            sb.append(" WHERE product_id = ?");
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, product.getProductId());
        }
        else if(product.getProductName() != null && !product.getProductName().isEmpty()){
            sb.append(" WHERE product_name = ?");
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, product.getProductName());
        }
        else{
            ps = con.prepareStatement(sb.toString());
        }

        ResultSet rs = ps.executeQuery();
        List<Product> productList = new ArrayList<>();
        while(rs.next()){
            Product p = new Product(
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("product_price")
            );
            productList.add(p);
        }
        rs.close();
        con.close();

        return productList;
    }

    public static void update(Product product) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        Map<String, Object> keyVal = new HashMap<>();

        if(product.getProductName() != null && !product.getProductName().isEmpty()){
            keyVal.put("product_name", product.getProductName());
        }
        if(!Double.isNaN(product.getProductPrice())){
            keyVal.put("product_price", product.getProductPrice());
        }

        StringBuilder sb = new StringBuilder("UPDATE product SET ");
        for (Map.Entry<String, Object> entry : keyVal.entrySet()){
            sb.append(" ").append(entry.getKey()).append(" = ?,");
        }
        sb.setLength(sb.length()-1);
        sb.append(" WHERE product_id = ? ");

        int i = 1;
        PreparedStatement ps = con.prepareStatement(sb.toString());
        for (Map.Entry<String, Object> entry : keyVal.entrySet()){
            if(entry.getKey().equals("product_price")){
                ps.setDouble(i++, (double)entry.getValue());
            }
            else{
                ps.setString(i++, (String)entry.getValue());
            }
        }
        ps.setString(i++, product.getProductId());

        ps.executeUpdate();
        con.close();
    }

    public static void delete(Product product) throws SQLException {
        Connection con = Database.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM product WHERE product_id = ?");
        ps.setString(1, product.getProductId());
        ps.executeUpdate();
        con.close();
    }
}
