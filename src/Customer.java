import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Customer extends Person {
    private String driverPermit;
    private int Id;

    public Customer(Scanner scanner) {
        super(scanner);
        System.out.print("Introdu tipul permisului: ");
        String localDriverPermit = sc.nextLine();
        if (isValidDriverPermit(localDriverPermit)) {
            this.driverPermit = localDriverPermit;
        } else {
            throw new InvalidDriverPermitException("Permisul introdus nu este valid in Romania.");
        }
    }

    public Customer(String name, String surname, String CNP, int age, String phoneNumber, String driverPermit, Scanner scanner) {
        super(name, surname, CNP, age, phoneNumber, scanner);
        if (isValidDriverPermit(driverPermit)) {
            this.driverPermit = driverPermit;
        } else {
            throw new InvalidDriverPermitException("Permisul introdus nu este valid în Romania.");
        }
    }

    private boolean isValidDriverPermit(String driverPermit) {
        List<String> validPermits = Arrays.asList(
                "A", "A1", "A2", "AM", "B", "B1", "BE",
                "C", "C1", "CE", "C1E", "D", "D1", "DE", "D1E", "Tb", "Tv"
        );
        return validPermits.contains(driverPermit);
    }

    public String getDriverPermit() {
        return driverPermit;
    }

    public int getID() {
        return Id;
    }

    public void setID(int ID) {
        Id = ID;
    }

    @Override
    public String toString() {
        return "Name: " + this.name
                + "\nSurname: " + this.surname
                + "\nId: " + this.Id
                + "\nCNP: " + this.CNP
                + "\nPhoneNumber: " + this.phoneNumber
                + "\nAge: " + this.age
                + "\nPermis: " + this.driverPermit
                + "\n-------------------------------\n";
    }
}
