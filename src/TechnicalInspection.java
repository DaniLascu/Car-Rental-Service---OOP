import java.time.LocalDate;

public class TechnicalInspection implements Comparable<TechnicalInspection> {
    private int id;
    private LocalDate expiryDate;
    private Vehicle vehicle;
    private Mechanic mechanic;

    public TechnicalInspection(Vehicle vehicle, Mechanic mechanic) {
        this.vehicle = vehicle;
        this.mechanic = mechanic;
        expiryDate = LocalDate.now().plusYears(1);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setExipryDate(LocalDate exipryDate) {
        this.expiryDate = exipryDate;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isValidInspection(){
        return !this.expiryDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "-----------Start Of Inspection-----------\n" +
                "Vehicle: " + vehicle.toString() + "\nMechanic:" + mechanic.toString() + "\nExpiry date: " + expiryDate.toString()
                + "\n-----------End Of Inspection----------\n";
    }

    @Override
    public int compareTo(TechnicalInspection obj) {

        if(expiryDate.isBefore(obj.expiryDate)) return -1;
        if(expiryDate.isAfter(obj.expiryDate)) return 1;
        return 0;
    }
}
