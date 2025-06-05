import java.time.LocalDate;

public class Accident {
    private int accidentId;
    private LocalDate accidentDate;
    private double accidentPrice;
    private int vehicleId; //FK

    public Accident(LocalDate accidentDate, double accidentPrice, int vehicleId) {
        this.accidentDate = accidentDate;
        this.accidentPrice = accidentPrice;
        this.vehicleId = vehicleId;
    }
    public LocalDate getAccidentDate() {
        return accidentDate;
    }
    public double getAccidentPrice() {
        return accidentPrice;
    }
    public int getVehicleId() {
        return vehicleId;
    }

    public void setAccidentId(int accidentId) {
        this.accidentId = accidentId;
    }
    public int getAccidentId() {
        return accidentId;
    }

    @Override
    public String toString() {
        return "Accident [accidentDate=" + accidentDate + ", accidentPrice=" + accidentPrice + "]";
    }
}
