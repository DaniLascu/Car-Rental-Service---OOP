import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VanService {

    public void createTables() {
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS vans (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    brand VARCHAR(100),
                    model VARCHAR(100),
                    color VARCHAR(50),
                    fabricationYear INT,
                    mileage DOUBLE,
                    fuelConsumption DOUBLE,
                    capacity DOUBLE,
                    availability BOOLEAN,
                    fuelType VARCHAR(50),
                    insuranceId INT,
                    weightCapacity DOUBLE,
                    insurancePrice DOUBLE,
                    FOREIGN KEY (fuelType) REFERENCES fuelTypes(fuelType) ON DELETE CASCADE,
                    FOREIGN KEY (insuranceId) REFERENCES insurances(id) ON DELETE CASCADE
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Van van) {
        String sql = "INSERT INTO vans "
                + "(brand, model, color, fabricationYear, mileage, fuelConsumption, capacity, availability, "
                + "fuelType, insuranceId, weightCapacity, insurancePrice) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, van.getBrand());
            ps.setString(2, van.getModel());
            ps.setString(3, van.getColor());
            ps.setInt(4, van.getFabricationYear());
            ps.setDouble(5, van.getMileage());
            ps.setDouble(6, van.getFuelConsumption());
            ps.setDouble(7, van.getCapacity());
            ps.setBoolean(8, van.getAvailability());
            ps.setString(9, van.getFuelType().getFuelType().name());
            ps.setObject(10, (van.getInsurance() != null ? van.getInsurance().getId() : null), Types.INTEGER);
            ps.setDouble(11, van.getWeightCapacity());
            ps.setDouble(12, Van.insurancePrice);

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    van.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Van get(int id, Scanner scanner, ArrayList<FuelType> fuelTypes) {
        InsuranceService insuranceService = new InsuranceService();
        String sql = "SELECT * FROM vans WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    String color = rs.getString("color");
                    int fabricationYear = rs.getInt("fabricationYear");
                    double mileage = rs.getDouble("mileage");
                    double fuelConsumption = rs.getDouble("fuelConsumption");
                    double capacity = rs.getDouble("capacity");
                    boolean availability = rs.getBoolean("availability");
                    String fuelTypeStr = rs.getString("fuelType");
                    double weightCapacity = rs.getDouble("weightCapacity");
                    int insuranceId = rs.getInt("insuranceId");
                    boolean hasInsurance = !rs.wasNull();
                    int vanId = rs.getInt("id");

                    FuelTypeEnum fuelEnum = FuelTypeEnum.valueOf(fuelTypeStr.toUpperCase());
                    FuelType fuelType = fuelTypes.stream()
                            .filter(ft -> ft.getFuelType().equals(fuelEnum))
                            .findFirst()
                            .orElse(null);

                    Insurance insurance = null;
                    if (hasInsurance) {
                        insurance = insuranceService.get(insuranceId);
                    }

                    Van van = new Van(
                            brand,
                            model,
                            color,
                            fabricationYear,
                            mileage,
                            fuelConsumption,
                            capacity,
                            fuelType,
                            weightCapacity,
                            scanner
                    );

                    van.setAvailability(availability);
                    van.setId(vanId);
                    van.setInsurance(insurance);

                    return van;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Van> getAll(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<Van> vans = new ArrayList<>();
        String sql = "SELECT id FROM vans";

        ArrayList<Integer> ids = new ArrayList<>();

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Integer id : ids) {
            Van van = get(id, scanner, fuelTypes);
            if (van != null) vans.add(van);
        }

        return vans;
    }

    public void updateMileage(int id, double newMileage) {
        String sql = "UPDATE vans SET mileage = ? WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newMileage);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateInsurance(int id, Insurance newInsurance) {
        String sql = "UPDATE vans SET insuranceId = ? WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, newInsurance.getId());
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM vans WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAvailability(int id, boolean newAvailability) {
        String sql = "UPDATE vans SET availability = ? WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, newAvailability);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
