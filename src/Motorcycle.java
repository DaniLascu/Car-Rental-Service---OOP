import java.time.LocalDate;
import java.util.Scanner;

enum MotorcycleType {
    CRUISER, SPORT, TOURING, STANDARD, DIRT
}

public class Motorcycle extends Vehicle {
    private boolean hasSidecar;
    private MotorcycleType type;
    private boolean hasStorageCompartment;
    final static double insurancePrice = 320.49;

    public Motorcycle(String brand, String model, String color, int fabricationYear,
                      double mileage, double fuelConsumption, double capacity,
                      FuelType fuelType, boolean hasSidecar,
                      MotorcycleType type, boolean hasStorageCompartment, Scanner scanner) {
        super(brand, model, color, fabricationYear, mileage, fuelConsumption, capacity, fuelType, scanner);
        this.hasSidecar = hasSidecar;
        this.type = type;
        this.hasStorageCompartment = hasStorageCompartment;
    }

    public boolean hasSidecar() {
        return hasSidecar;
    }

    public MotorcycleType getType() {
        return type;
    }

    public boolean hasStorageCompartment() {
        return hasStorageCompartment;
    }

    public double getInsurancePrice() {
        return insurancePrice;
    }

    @Override
    public void renewInsurance() {
        int duration;
        do {
            System.out.print("Enter insurance duration in years (>0): ");
            duration = scanner.nextInt();
        } while (duration <= 0);

        double cost = duration * insurancePrice;
        System.out.println("Insurance will cost: $" + cost);
        this.insurance = new Insurance(this.Id ,LocalDate.now().plusYears(duration), cost);
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "ID: " + Id + "\n" +
                "Brand=" + brand +
                "\nModel=" + model +
                "\nType=" + type +
                "\nSidecar=" + (hasSidecar ? "Yes" : "No") +
                "\nStorage Compartment=" + (hasStorageCompartment ? "Yes" : "No") +
                "\nFabrication Year=" + fabricationYear +
                "\nMileage=" + mileage +
                "\nFuel Consumption=" + fuelConsumption +
                "\nCapacity=" + capacity +
                "\n" + fuelType +
                "\nInsurance=" + (insurance != null ? insurance : "Not insured") +
                '}';
    }
}
