import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TechnicalInspectionService {
    private String getTechnicalInspectingTable(Vehicle vehicle) {
        if (vehicle instanceof Car) return "technicalInspectionsForCars";
        if (vehicle instanceof Van) return "technicalInspectionsForVans";
        if (vehicle instanceof Motorcycle) return "technicalInspectionsForMotorcycles";

        throw new IllegalArgumentException("Invalid vehicle type");
    }

    public void createTables(){
        createTable("technicalInspectionsForCars", "cars");
        createTable("technicalInspectionsForVans", "vans");
        createTable("technicalInspectionsForMotorcycles", "motorcycles");
    }

    public void createTable(String inspectionTable, String vehicleTable) {
        String sql = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    expiryDate DATE NOT NULL,
                    vehicleId INT NOT NULL,
                    mechanicId INT NOT NULL,
                    FOREIGN KEY (vehicleId) REFERENCES %s(id) ON DELETE CASCADE,
                    FOREIGN KEY (mechanicId) REFERENCES mechanics(id) ON DELETE CASCADE
                )
                """, inspectionTable, vehicleTable);

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TechnicalInspection insert(TechnicalInspection technicalInspection, Vehicle vehicle, Mechanic mechanic) {
        String table = getTechnicalInspectingTable(vehicle);
        String sql = "INSERT INTO " + table + " (expiryDate, vehicleId, mechanicId) VALUES (?, ?, ?)";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, java.sql.Date.valueOf(technicalInspection.getExpiryDate()));
            ps.setInt(2, vehicle.getId());
            ps.setInt(3, mechanic.getID());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    technicalInspection.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicalInspection;
    }

    public ArrayList<TechnicalInspection> getAllFromCars(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<TechnicalInspection> inspections = new ArrayList<>();
        String sql = "SELECT * FROM technicalInspectionsForCars";
        CarService carService = new CarService();
        MechanicService mechanicService = new MechanicService();

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Integer> vehicleIds = new ArrayList<>();
            List<Integer> mechanicIds = new ArrayList<>();
            List<Integer> inspectionIds = new ArrayList<>();
            List<LocalDate> expiryDates = new ArrayList<>();

            while (rs.next()) {
                inspectionIds.add(rs.getInt("id"));
                vehicleIds.add(rs.getInt("vehicleId"));
                mechanicIds.add(rs.getInt("mechanicId"));
                expiryDates.add(rs.getDate("expiryDate").toLocalDate());
            }

            for (int i = 0; i < inspectionIds.size(); i++) {
                Car car = carService.get(vehicleIds.get(i), scanner, fuelTypes);
                Mechanic mechanic = mechanicService.getMechanicById(mechanicIds.get(i), scanner);

                TechnicalInspection inspection = new TechnicalInspection((Vehicle) car, mechanic);
                inspection.setId(inspectionIds.get(i));
                inspection.setExipryDate(expiryDates.get(i));
                inspections.add(inspection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inspections;
    }

    public ArrayList<TechnicalInspection> getAllFromVans(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<TechnicalInspection> inspections = new ArrayList<>();
        String sql = "SELECT * FROM technicalInspectionsForVans";
        VanService vanService = new VanService();
        MechanicService mechanicService = new MechanicService();

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Integer> vehicleIds = new ArrayList<>();
            List<Integer> mechanicIds = new ArrayList<>();
            List<Integer> inspectionIds = new ArrayList<>();
            List<LocalDate> expiryDates = new ArrayList<>();

            while (rs.next()) {
                inspectionIds.add(rs.getInt("id"));
                vehicleIds.add(rs.getInt("vehicleId"));
                mechanicIds.add(rs.getInt("mechanicId"));
                expiryDates.add(rs.getDate("expiryDate").toLocalDate());
            }

            for (int i = 0; i < inspectionIds.size(); i++) {
                Van van = vanService.get(vehicleIds.get(i), scanner, fuelTypes);
                Mechanic mechanic = mechanicService.getMechanicById(mechanicIds.get(i), scanner);

                TechnicalInspection inspection = new TechnicalInspection((Vehicle) van, mechanic);
                inspection.setId(inspectionIds.get(i));
                inspection.setExipryDate(expiryDates.get(i));
                inspections.add(inspection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inspections;
    }

    public ArrayList<TechnicalInspection> getAllFromMotorcycles(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<TechnicalInspection> inspections = new ArrayList<>();
        String sql = "SELECT * FROM technicalInspectionsForMotorcycles";
        MotorcycleService motorcycleService = new MotorcycleService();
        MechanicService mechanicService = new MechanicService();

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Integer> vehicleIds = new ArrayList<>();
            List<Integer> mechanicIds = new ArrayList<>();
            List<Integer> inspectionIds = new ArrayList<>();
            List<LocalDate> expiryDates = new ArrayList<>();

            while (rs.next()) {
                inspectionIds.add(rs.getInt("id"));
                vehicleIds.add(rs.getInt("vehicleId"));
                mechanicIds.add(rs.getInt("mechanicId"));
                expiryDates.add(rs.getDate("expiryDate").toLocalDate());
            }

            for (int i = 0; i < inspectionIds.size(); i++) {
                Motorcycle motorcycle = motorcycleService.get(vehicleIds.get(i), scanner, fuelTypes);
                Mechanic mechanic = mechanicService.getMechanicById(mechanicIds.get(i), scanner);

                TechnicalInspection inspection = new TechnicalInspection((Vehicle) motorcycle, mechanic);
                inspection.setId(inspectionIds.get(i));
                inspection.setExipryDate(expiryDates.get(i));
                inspections.add(inspection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inspections;
    }

}
