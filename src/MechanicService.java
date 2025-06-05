import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MechanicService {

    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS mechanics (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                surname VARCHAR(50) NOT NULL,
                cnp VARCHAR(13) UNIQUE NOT NULL,
                age INT NOT NULL,
                phoneNumber VARCHAR(20) NOT NULL,
                salary DOUBLE NOT NULL
            )
        """;

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Mechanic insertMechanic(Mechanic mechanic) {
        String sql = """
            INSERT INTO mechanics (name, surname, cnp, age, phoneNumber, salary)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, mechanic.getName());
            ps.setString(2, mechanic.getSurname());
            ps.setString(3, mechanic.getCNP());
            ps.setInt(4, mechanic.getAge());
            ps.setString(5, mechanic.getPhoneNumber());
            ps.setDouble(6, mechanic.getSalary());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    mechanic.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mechanic;
    }

    public ArrayList<Mechanic> getAllMechanics(Scanner scanner) {
        ArrayList<Mechanic> mechanics = new ArrayList<>();
        String sql = "SELECT * FROM mechanics";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mechanic m = new Mechanic(
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("cnp"),
                        rs.getInt("age"),
                        rs.getString("phoneNumber"),
                        rs.getDouble("salary"),
                        scanner
                );
                m.setId(rs.getInt("id"));
                mechanics.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mechanics;
    }

    public void deleteMechanicById(int id) {
        String sql = "DELETE FROM mechanics WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMechanicSalary(int id, double newSalary) {
        String sql = "UPDATE mechanics SET salary = ? WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newSalary);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Mechanic getMechanicById(int id, Scanner scanner) {
        String sql = "SELECT * FROM mechanics WHERE id = ?";
        Mechanic mechanic = null;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mechanic = new Mechanic(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("cnp"),
                            rs.getInt("age"),
                            rs.getString("phoneNumber"),
                            rs.getDouble("salary"),
                            scanner
                    );
                    mechanic.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mechanic;
    }

}
