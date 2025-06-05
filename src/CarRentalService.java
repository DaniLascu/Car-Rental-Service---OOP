import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarRentalService {
    private String getCarRentalTable(Vehicle vehicle) {
        if (vehicle instanceof Car) return "carRentalsForCars";
        if (vehicle instanceof Van) return "carRentalsForVans";
        if (vehicle instanceof Motorcycle) return "carRentalsForMotorcycles";

        throw new IllegalArgumentException("Invalid vehicle type");
    }

    public void createTables(){
        createTable("carRentalsForCars", "cars");
        createTable("carRentalsForVans", "vans");
        createTable("carRentalsForMotorcycles", "motorcycles");
    }

    public void createTable(String rentalTable, String vehicleTable) {
        String sql = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    startDate DATE NOT NULL,
                    endDate DATE NOT NULL,
                    vehicleId INT NOT NULL,
                    customerId INT NOT NULL,
                    FOREIGN KEY (vehicleId) REFERENCES %s(id) ON DELETE CASCADE,
                    FOREIGN KEY (customerId) REFERENCES customers(id) ON DELETE CASCADE
                )
                """, rentalTable, vehicleTable);

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CarRental insert(CarRental carRental, Vehicle vehicle, Customer customer) {
        String table = getCarRentalTable(vehicle);
        String sql = "INSERT INTO " + table + " (startDate, endDate, vehicleId, customerId) VALUES (?, ?, ?, ?)";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, java.sql.Date.valueOf(carRental.getStartDate()));
            ps.setDate(2, java.sql.Date.valueOf(carRental.getEndDate()));
            ps.setInt(3, vehicle.getId());
            ps.setInt(4, customer.getID());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    carRental.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carRental;
    }

    public ArrayList<CarRental> getAllRentalsForCars(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<CarRental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM carRentalsForCars";
        CarService carService = new CarService();
        CustomerService customerService = new CustomerService();

        List<Integer> ids = new ArrayList<>();
        List<LocalDate> startDates = new ArrayList<>();
        List<LocalDate> endDates = new ArrayList<>();
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> customerIds = new ArrayList<>();

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
                startDates.add(rs.getDate("startDate").toLocalDate());
                endDates.add(rs.getDate("endDate").toLocalDate());
                vehicleIds.add(rs.getInt("vehicleId"));
                customerIds.add(rs.getInt("customerId"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ids.size(); i++) {
            Car car = carService.get(vehicleIds.get(i), scanner, fuelTypes);
            Customer customer = customerService.getCustomerById(customerIds.get(i), scanner);

            CarRental rental = new CarRental(car, customer);
            rental.setId(ids.get(i));
            rental.setEndDate(endDates.get(i));
            rentals.add(rental);
        }

        return rentals;
    }

    public ArrayList<CarRental> getAllRentalsForVans(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<CarRental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM carRentalsForVans";
        VanService vanService = new VanService();
        CustomerService customerService = new CustomerService();

        List<Integer> ids = new ArrayList<>();
        List<LocalDate> startDates = new ArrayList<>();
        List<LocalDate> endDates = new ArrayList<>();
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> customerIds = new ArrayList<>();

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
                startDates.add(rs.getDate("startDate").toLocalDate());
                endDates.add(rs.getDate("endDate").toLocalDate());
                vehicleIds.add(rs.getInt("vehicleId"));
                customerIds.add(rs.getInt("customerId"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ids.size(); i++) {
            Van van = vanService.get(vehicleIds.get(i), scanner, fuelTypes);
            Customer customer = customerService.getCustomerById(customerIds.get(i), scanner);

            CarRental rental = new CarRental(van, customer);
            rental.setId(ids.get(i));
            rental.setEndDate(endDates.get(i));
            rentals.add(rental);
        }

        return rentals;
    }

    public ArrayList<CarRental> getAllRentalsForMotorcycles(Scanner scanner, ArrayList<FuelType> fuelTypes) {
        ArrayList<CarRental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM carRentalsForMotorcycles";
        MotorcycleService motorcycleService = new MotorcycleService();
        CustomerService customerService = new CustomerService();

        List<Integer> ids = new ArrayList<>();
        List<LocalDate> startDates = new ArrayList<>();
        List<LocalDate> endDates = new ArrayList<>();
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> customerIds = new ArrayList<>();

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
                startDates.add(rs.getDate("startDate").toLocalDate());
                endDates.add(rs.getDate("endDate").toLocalDate());
                vehicleIds.add(rs.getInt("vehicleId"));
                customerIds.add(rs.getInt("customerId"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ids.size(); i++) {
            Motorcycle motorcycle = motorcycleService.get(vehicleIds.get(i), scanner, fuelTypes);
            Customer customer = customerService.getCustomerById(customerIds.get(i), scanner);

            CarRental rental = new CarRental(motorcycle, customer);
            rental.setId(ids.get(i));
            rental.setEndDate(endDates.get(i));
            rentals.add(rental);
        }

        return rentals;
    }



}
