import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InsuranceService {
    public void createTable() {
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS insurances (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    expiryDate DATE NOT NULL,
                    price DOUBLE NOT NULL
                )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(LocalDate expiryDate, double price) {
        String sql = "INSERT INTO insurances (expiryDate, price) VALUES (?, ?)";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(expiryDate));
            ps.setDouble(2, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Insurance get(int id) {
        String sql = "SELECT * FROM insurances WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate expiryDate = rs.getDate("expiryDate").toLocalDate();
                    double price = rs.getDouble("price");

                    Insurance insurance = new Insurance(id,expiryDate, price);

                    return insurance;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Not found
    }

    public List<Insurance> getAll() {
        List<Insurance> list = new ArrayList<>();
        String sql = "SELECT * FROM insurances";
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate expiryDate = rs.getDate("expiryDate").toLocalDate();
                double price = rs.getDouble("price");
                int id = rs.getInt("id");
                list.add(new Insurance(id, expiryDate, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(int id, LocalDate newExpiry, double newPrice) {
        String sql = "UPDATE insurances SET expiryDate = ?, price = ? WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(newExpiry));
            ps.setDouble(2, newPrice);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM insurances WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
