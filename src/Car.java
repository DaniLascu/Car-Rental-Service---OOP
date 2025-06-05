import java.time.LocalDate;
import java.util.Scanner;

public class Car extends Vehicle {
    private int seatsNumber;
    final static double insurancePrice = 550.99;

    public Car(String brand, String model, String color, int fabricationYear, double mileage,
               double fuelConsumption, double capacity, FuelType fuelType, int seatsNumber, Scanner scanner) {
        super(brand, model, color, fabricationYear, mileage, fuelConsumption, capacity, fuelType, scanner);
        this.seatsNumber = seatsNumber;
    }
    public int getSeatsNumber() {
        return seatsNumber;
    }
    public double getInsurancePrice() {
        return insurancePrice;
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
                "- Seats: " + seatsNumber + "\n" +
                fuelType + "\n" +
                "- Insurance: " + (insurance != null ? insurance : "Not insured") + "\n";
    }

}
