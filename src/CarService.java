import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarService {

    public void createTables() {
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cars (
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
                    seatsNumber INT,
                    insurancePrice DOUBLE,
                    FOREIGN KEY (fuelType) REFERENCES fuelTypes(fuelType) ON DELETE CASCADE,
                    FOREIGN KEY (insuranceId) REFERENCES insurances(id) ON DELETE CASCADE
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Car car) {
        String sql = "INSERT INTO cars ( brand, model, color, fabricationYear, mileage, fuelConsumption, capacity, availability, fuelType, insuranceId, seatsNumber, insurancePrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setString(3, car.getColor());
            ps.setInt(4, car.getFabricationYear());
            ps.setDouble(5, car.getMileage());
            ps.setDouble(6, car.getFuelConsumption());
            ps.setDouble(7, car.getCapacity());
            ps.setBoolean(8, car.getAvailability());
            ps.setString(9, car.getFuelType().getFuelType().name());
            ps.setObject(10, (car.getInsurance() != null ? car.getInsurance().getId() : null), Types.INTEGER);
            ps.setInt(11, car.getSeatsNumber());
            ps.setDouble(12, car.getInsurancePrice());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Car get(int id, Scanner scanner, ArrayList<FuelType> fuelTypes) {

        InsuranceService insuranceService = new InsuranceService();
        String sql = "SELECT * FROM cars WHERE id = ?";

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
                    int seatsNumber = rs.getInt("seatsNumber");
                    int insuranceId = rs.getInt("insuranceId");
                    boolean hasInsurance = !rs.wasNull();
                    int carId = rs.getInt("id");

                    FuelTypeEnum fuelEnum = FuelTypeEnum.valueOf(fuelTypeStr.toUpperCase());

                    FuelType fuelType = fuelTypes.stream()
                            .filter(ft -> ft.getFuelType().equals(fuelEnum))
                            .findFirst()
                            .orElse(null);

                    Insurance insurance = null;
                    if (hasInsurance) {
                        insurance = insuranceService.get(insuranceId);
                    }

                    Car car = new Car(
                            brand,
                            model,
                            color,
                            fabricationYear,
                            mileage,
                            fuelConsumption,
                            capacity,
                            fuelType,
                            seatsNumber,
                            scanner
                    );

                    car.setAvailability(availability);
                    car.setId(carId);
                    car.setInsurance(insurance);

                    return car;

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ArrayList<Car> getAll(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<Car> cars = new ArrayList<>();
        String sql = "SELECT id FROM cars";

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
            Car car = get(id, scanner, fuelTypes);
            if (car != null) cars.add(car);
        }

        return cars;
    }

    public void updateMileage(int id, double newMileage) {
        String sql = "UPDATE cars SET mileage = ? WHERE id = ?";

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
        String sql = "UPDATE cars SET insuranceId = ? WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newInsurance.getId());
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAvailability(int id, boolean newAvailability) {
        String sql = "UPDATE cars SET availability = ? WHERE id = ?";

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
