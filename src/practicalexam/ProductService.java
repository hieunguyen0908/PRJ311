package practicalexam;

import java.sql.*;
import java.util.*;

/**
 *
 * @author PC
 */
public class ProductService {

    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String url = "jdbc:sqlserver://HIEUCAN\\MINHHIEU:1433; databaseName=ProductDB;"
            + "user=sa; password=123456";

    public static Connection openConnection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }
    
    public static Vector<Product> getAllProducts() throws Exception {
        Vector<Product> list = new Vector<>();
        try (Connection c = openConnection();
                Statement sm = c.createStatement()) {
            ResultSet rs = sm.executeQuery("Select * From Products");
            while (rs.next()) {
                Product p = new Product(rs.getInt("ID"), rs.getString("Name"), rs.getFloat("Price"));
                list.add(p);
            }
        }
        return list;
    }

    public Product getProductByID(int ID) throws Exception {
        String query = "Select * From Products Where ID = ?";
        try (Connection c = openConnection();
                PreparedStatement ps = c.prepareStatement(query)) {
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getFloat("Price"));
            }
        }
        return null;
    }

    public static int update(Product p) throws Exception{
        String sql = "Update Items set Name=?, Price=? Where ID = ?";
        try(Connection c = openConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setFloat(2, p.getPrice());
            ps.setInt(3, p.getID());
            return ps.executeUpdate();
        }
    }

    public int insert(Product p) throws Exception {
        String query = "insert into Products Values(?,?,?)";
        try (Connection c = openConnection();
                PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, p.getID());
            ps.setString(2, p.getName().trim());
            ps.setFloat(3, p.getPrice());
            return ps.executeUpdate();
        }
    }

    public int remove(int ID) throws Exception {
        String query = "Delete From Products Where ID = ?";
        try (Connection c = openConnection();
                PreparedStatement ps = c.prepareStatement(query)) {
            ps.setInt(1, ID);
            return ps.executeUpdate();
        }
    }

    public static Vector<Product> searchByName(String keyword) throws Exception {
        Vector<Product> list = new Vector<>();
        String sql = "Select * From Products Where Name Like ?";
        try (Connection c = openConnection();
                PreparedStatement sm = c.prepareStatement(sql)) {
            sm.setString(1, "%" + keyword + "%");
            ResultSet rs = sm.executeQuery();
            while (rs.next()) {
                Product p = new Product(rs.getInt("ID"), rs.getString("Name"), rs.getFloat("Price"));
                list.add(p);
            }
        }
        return list;
    }
}
