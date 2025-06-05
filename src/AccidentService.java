import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AccidentService {

    private String getAccidentTable(Vehicle vehicle) {
        if (vehicle instanceof Car) return "accidentsForCars";
        if (vehicle instanceof Van) return "accidentsForVans";
        if (vehicle instanceof Motorcycle) return "accidentsForMotorcycles";

        throw new IllegalArgumentException("Unknown vehicle type");
    }

    public void createTables() {
        createTable("accidentsForCars", "cars");
        createTable("accidentsForVans", "vans");
        createTable("accidentsForMotorcycles", "motorcycles");
    }

    private void createTable(String accidentTable, String vehicleTable) {
        String sql = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    accidentDate DATE NOT NULL,
                    accidentPrice DOUBLE NOT NULL,
                    vehicleId INT NOT NULL,
                    FOREIGN KEY (vehicleId) REFERENCES %s(id) ON DELETE CASCADE
                )
                """, accidentTable, vehicleTable);

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Accident insertAccident(Accident accident, Vehicle vehicle) {
        String table = getAccidentTable(vehicle);
        String sql = "INSERT INTO " + table + " (accidentDate, accidentPrice, vehicleId) VALUES (?, ?, ?)";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(accident.getAccidentDate()));
            ps.setDouble(2, accident.getAccidentPrice());
            ps.setInt(3, vehicle.getId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    accident.setAccidentId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accident;
    }

    public ArrayList<Accident> getAllAccidentsForVehicle(Vehicle vehicle) {
        ArrayList<Accident> accidents = new ArrayList<>();
        String table = getAccidentTable(vehicle);
        String sql = "SELECT * FROM " + table + " WHERE vehicleId = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicle.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Accident accident = new Accident(
                            rs.getDate("accidentDate").toLocalDate(),
                            rs.getDouble("accidentPrice"),
                            rs.getInt("vehicleId")
                    );
                    accident.setAccidentId(rs.getInt("id"));
                    accidents.add(accident);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accidents;
    }

    public void deleteByVehicle(Vehicle vehicle) {
        String table = getAccidentTable(vehicle);
        String sql = "DELETE FROM " + table + " WHERE vehicleId = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicle.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        for (String table : new String[]{"accidentsForCars", "accidentsForVans", "accidentsForMotorcycles"}) {
            try (Connection con = DbConnection.getInstance();
                 Statement stmt = con.createStatement()) {
                stmt.executeUpdate("DELETE FROM " + table);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
