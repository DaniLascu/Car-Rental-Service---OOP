import java.time.*;

public class Insurance {
    private LocalDate expiryDate;
    private double price;
    private int id;

    public Insurance() {
        this.expiryDate = LocalDate.now().plusYears(1);
        this.price = 499.99;
    }
    public Insurance(int id, LocalDate expiryDate, double price) {
        this.expiryDate = expiryDate;
        this.price = price;
        this.id = id;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Expiration Date: " + this.expiryDate + "\nPrice: " + this.price + "\n";
    }
}