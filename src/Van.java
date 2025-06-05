import java.time.LocalDate;
import java.util.Scanner;

public class Van extends Vehicle{
    private double weightCapacity;
    final static double insurancePrice = 659.99;

    public Van(String brand, String model, String color, int fabricationYear, double mileage,
               double fuelConsumption, double capacity, FuelType fuelType, double weightCapacity, Scanner scanner) {
        super(brand, model, color, fabricationYear, mileage, fuelConsumption, capacity, fuelType, scanner);
        this.weightCapacity = weightCapacity;
    }
    public double getWeightCapacity() {
        return weightCapacity;
    }
    @Override
    public void renewInsurance() {
        int localDuration;
        double localPrice;

        do {
            System.out.print("Choose the duration of the insurance (in years, must be > 0): ");
            localDuration = scanner.nextInt();
        } while (localDuration <= 0);

        System.out.println("The price of the Insurance will be  " + localDuration * insurancePrice);
        localPrice = localDuration * insurancePrice;


        this.insurance = new Insurance(this.Id, LocalDate.now().plusYears(localDuration), localPrice);
    }

    @Override
    public String toString() {
        return "Car Details:\n" +
                "- Id: " + Id + "\n" +
                "- Brand: " + brand + "\n" +
                "- Model: " + model + "\n" +
                "- Color: " + color + "\n" +
                "- Fabrication Year: " + fabricationYear + "\n" +
                "- Mileage: " + mileage + " km\n" +
                "- Fuel Consumption: " + fuelConsumption + " L/100km\n" +
                "- Capacity: " + capacity + "\n" +
                "- Weight it can carry: " + weightCapacity + "\n" +
                fuelType + "\n" +
                "- Insurance: " + (insurance != null ? insurance : "Not insured") + "\n";
    }
}
