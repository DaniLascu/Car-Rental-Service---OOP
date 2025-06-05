import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MotorcycleService {

    public void createTables() {
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS motorcycles (
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
                    hasSidecar BOOLEAN,
                    motorcycleType VARCHAR(50),
                    hasStorage BOOLEAN,
                    insurancePrice DOUBLE,
                    FOREIGN KEY (fuelType) REFERENCES fuelTypes(fuelType) ON DELETE CASCADE,
                    FOREIGN KEY (insuranceId) REFERENCES insurances(id) ON DELETE CASCADE
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Motorcycle motorcycle) {
        String sql = "INSERT INTO motorcycles "
                + "(brand, model, color, fabricationYear, mileage, fuelConsumption, capacity, availability, "
                + "fuelType, insuranceId, hasSidecar, motorcycleType, hasStorage, insurancePrice) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, motorcycle.getBrand());
            ps.setString(2, motorcycle.getModel());
            ps.setString(3, motorcycle.getColor());
            ps.setInt(4, motorcycle.getFabricationYear());
            ps.setDouble(5, motorcycle.getMileage());
            ps.setDouble(6, motorcycle.getFuelConsumption());
            ps.setDouble(7, motorcycle.getCapacity());
            ps.setBoolean(8, motorcycle.getAvailability());
            ps.setString(9, motorcycle.getFuelType().getFuelType().name());
            ps.setObject(10, (motorcycle.getInsurance() != null ? motorcycle.getInsurance().getId() : null), Types.INTEGER);
            ps.setBoolean(11, motorcycle.hasSidecar());
            ps.setString(12, motorcycle.getType().name());
            ps.setBoolean(13, motorcycle.hasStorageCompartment());
            ps.setDouble(14, motorcycle.getInsurancePrice());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    motorcycle.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Motorcycle get(int id, Scanner scanner, ArrayList<FuelType> fuelTypes) {

        InsuranceService insuranceService = new InsuranceService();
        String sql = "SELECT * FROM motorcycles WHERE id = ?";

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
                    boolean hasSidecar = rs.getBoolean("hasSidecar");
                    boolean hasStorageCompartment = rs.getBoolean("hasStorage");
                    int insuranceId = rs.getInt("insuranceId");
                    boolean hasInsurance = !rs.wasNull();
                    int motorcycleId = rs.getInt("id");
                    String motorcycleTypeStr = rs.getString("motorcycleType");

                    FuelTypeEnum fuelEnum = FuelTypeEnum.valueOf(fuelTypeStr.toUpperCase());

                    FuelType fuelType = fuelTypes.stream()
                            .filter(ft -> ft.getFuelType().equals(fuelEnum))
                            .findFirst()
                            .orElse(null);

                    Insurance insurance = null;
                    if (hasInsurance) {
                        insurance = insuranceService.get(insuranceId);
                    }

                    Motorcycle motorcycle = new Motorcycle(
                            brand,
                            model,
                            color,
                            fabricationYear,
                            mileage,
                            fuelConsumption,
                            capacity,
                            fuelType,
                            hasSidecar,
                            MotorcycleType.valueOf(motorcycleTypeStr.toUpperCase()),
                            hasStorageCompartment,
                            scanner
                    );

                    motorcycle.setAvailability(availability);
                    motorcycle.setId(motorcycleId);
                    motorcycle.setInsurance(insurance);

                    return motorcycle;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ArrayList<Motorcycle> getAll(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<Motorcycle> motorcycles = new ArrayList<>();
        String sql = "SELECT id FROM motorcycles";

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
            Motorcycle motorcycle = get(id, scanner, fuelTypes);
            if (motorcycle != null) motorcycles.add(motorcycle);
        }

        return motorcycles;
    }

    public void updateMileage(int id, double newMileage) {
        String sql = "UPDATE motorcycles SET mileage = ? WHERE id = ?";

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
        String sql = "UPDATE motorcycles SET insuranceId = ? WHERE id = ?";

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
        String sql = "DELETE FROM motorcycles WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAvailability(int id, boolean newAvailability) {
        String sql = "UPDATE motorcycles SET availability = ? WHERE id = ?";

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
