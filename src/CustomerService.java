import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS customers (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                surname VARCHAR(50) NOT NULL,
                cnp VARCHAR(13) UNIQUE NOT NULL,
                age INT NOT NULL,
                phoneNumber VARCHAR(20) NOT NULL,
                driverPermit VARCHAR(20) NOT NULL
            )
        """;

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer insertCustomer(Customer customer) {
        String sql = """
            INSERT INTO customers (name, surname, cnp, age, phoneNumber, driverPermit)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getSurname());
            ps.setString(3, customer.getCNP());
            ps.setInt(4, customer.getAge());
            ps.setString(5, customer.getPhoneNumber());
            ps.setString(6, customer.getDriverPermit());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    customer.setID(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public ArrayList<Customer> getAllCustomers(Scanner scanner) {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("cnp"),
                        rs.getInt("age"),
                        rs.getString("phoneNumber"),
                        rs.getString("driverPermit"),
                        scanner
                );
                c.setID(rs.getInt("id"));
                customers.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public void deleteCustomerById(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Customer getCustomerById(int id, Scanner scanner) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        Customer customer = null;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("cnp"),
                            rs.getInt("age"),
                            rs.getString("phoneNumber"),
                            rs.getString("driverPermit"),
                            scanner
                    );
                    customer.setID(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

}
