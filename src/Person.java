import java.util.Scanner;

abstract class Person {
    protected String name;
    protected String surname;
    protected String CNP;
    protected int age;
    protected String phoneNumber;
    protected Scanner sc;

    public Person(Scanner scanner) {
        this.sc = scanner;
        System.out.println("Enter Name: ");
        this.name = sc.nextLine();

        System.out.println("Enter Surname: ");
        this.surname = sc.nextLine();

        System.out.println("Enter CNP: ");
        String localCnp = sc.nextLine();
        while(!isValidCNP(localCnp)) {
            System.out.println("Invalid CNP");
            System.out.println("Enter CNP: ");
            localCnp = sc.nextLine();

        }
        this.CNP = localCnp;

        System.out.println("Enter Age: ");
        int localAge = sc.nextInt();
        while(localAge < 0 || localAge > 120 ) {
            System.out.println("Invalid Age");
            System.out.println("Enter Age: ");
            localAge = sc.nextInt();
        }
        this.age = localAge;

        System.out.println("Enter Phone Number: ");
        sc.nextLine();
        String localPhone = sc.nextLine();
        while(!isValidPhone(localPhone)) {
            System.out.println("Phone Number - INVALID! ");
            System.out.print("Enter Phone Number: ");
            localPhone = sc.nextLine();
        }
        this.phoneNumber = localPhone;

    }

    public Person(String name, String surname, String CNP, int age, String phoneNumber, Scanner scanner) {
        this.sc = scanner;
        this.name = name;
        this.surname = surname;
        this.age = age;

        while(!isValidCNP(CNP)){
            System.out.println("CNP - INVALID! ");
            System.out.print("Enter CNP: ");
            CNP = sc.nextLine();
        }
        this.CNP = CNP;

        while(!isValidPhone(phoneNumber)){
            System.out.println("Phone Number - INVALID! ");
            System.out.print("Enter PHONE: ");
            phoneNumber = sc.nextLine();
        }
        this.phoneNumber = phoneNumber;

    }

    private boolean isValidCNP(String CNP) {
        String pattern = "^[1-9][0-9]{12}$";
        return CNP.matches(pattern);
    }
    private boolean isValidPhone(String phoneNumber) {
        String pattern = "^07[0-9]{8}$";
        return phoneNumber.matches(pattern);
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getCNP() {
        return CNP;
    }
    public int getAge() {
        return age;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
