import java.util.Scanner;

enum FuelTypeEnum {
    GASOLINE,
    DIESEL,
    GPL
}

public class FuelType {
    private FuelTypeEnum fuelType;
    private double pricePerGallon;

    public FuelType(FuelTypeEnum fuelType, double pricePerGallon) {
        this.fuelType = fuelType;
        this.pricePerGallon = pricePerGallon;
    }

    public FuelTypeEnum getFuelType() {
        return fuelType;
    }
    public double getPricePerGallon() {
        return pricePerGallon;
    }
    public void setPricePerGallon(double pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
    }

    @Override
    public String toString() {
        return "Fuel Type: " + fuelType + " Price: " + pricePerGallon + "\n";
    }
}
