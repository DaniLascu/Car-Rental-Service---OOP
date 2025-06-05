import java.time.LocalDate;

public class CarRental implements Comparable<CarRental>{
    private int id;
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate;
    private Vehicle vehicle;
    private Customer customer;

    public CarRental(Vehicle vehicle, Customer customer) {
        this.vehicle = vehicle;
        this.customer = customer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CarRental [startDate=" + startDate + ", endDate=" + endDate +
                "\nvehicle=\n" + vehicle + "\ncustomer=\n" + customer + "]";
    }

    @Override
    public int compareTo(CarRental o) {
        if(startDate.isBefore(o.startDate)) return -1;
        if(startDate.isAfter(o.startDate)) return 1;
        return 0;
    }
}
