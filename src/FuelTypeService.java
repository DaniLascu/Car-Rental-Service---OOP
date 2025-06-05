import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuelTypeService {
    public void createTable() {
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS fuelTypes (
            fuelType VARCHAR(50) PRIMARY KEY,
            pricePerGallon DOUBLE
        )
    """;

        String insertGasoline = "INSERT IGNORE INTO fuelTypes (fuelType, pricePerGallon) VALUES ('GASOLINE', 3.25)";
        String insertDiesel = "INSERT IGNORE INTO fuelTypes (fuelType, pricePerGallon) VALUES ('DIESEL', 5.99)";
        String insertGPL = "INSERT IGNORE INTO fuelTypes (fuelType, pricePerGallon) VALUES ('GPL', 1.39)";

        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(createTableSQL);
            stmt.executeUpdate(insertGasoline);
            stmt.executeUpdate(insertDiesel);
            stmt.executeUpdate(insertGPL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<FuelType> getAll() {
        ArrayList<FuelType> list = new ArrayList<>();
        String sql = "SELECT * FROM fuelTypes;";
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String fuelTypeStr = rs.getString("fuelType");
                double price = rs.getDouble("pricePerGallon");

                try {
                    FuelTypeEnum fuelEnum = FuelTypeEnum.valueOf(fuelTypeStr.toUpperCase());
                    list.add(new FuelType(fuelEnum, price));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid fuel type in DB: " + fuelTypeStr);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updatePricePerGallon(FuelTypeEnum fuelType, double newPrice) {
        String sql = "UPDATE fuelTypes SET pricePerGallon = ? WHERE fuelType = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, fuelType.name());  // Use enum name as String, e.g. "GASOLINE"

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No fuel type found with name: " + fuelType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
