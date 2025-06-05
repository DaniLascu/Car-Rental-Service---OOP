import java.util.Scanner;

public class Mechanic extends Person {
    private double salary;
    private int Id;


    public Mechanic(Scanner scanner) {
        super(scanner);
        System.out.print("Enter salary: ");
        this.salary = Double.parseDouble(sc.nextLine());
    }
    public Mechanic(String name, String surname, String CNP, int age, String phoneNumber, double salary, Scanner scanner) {
        super(name, surname, CNP, age, phoneNumber, scanner);
        this.salary = salary;
    }
    public double getSalary() {
        return salary;
    }
    public int getID() {
        return Id;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "Name: " + this.name
                + "\nSurname: " + this.surname
                + "\nId: " + this.Id
                + "\nCNP: " + this.CNP
                + "\nPhoneNumber: " + this.phoneNumber
                + "\nAge: " + this.age
                + "\nSalary: " + this.salary
                + "\n-----------------------------------------\n";
    }
}