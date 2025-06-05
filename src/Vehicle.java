import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Vehicle implements Comparable<Vehicle>, VehicleInterface{
    protected String brand;
    protected String model;
    protected String color;
    protected int fabricationYear;
    protected double mileage;
    protected double fuelConsumption;
    protected double capacity;
    protected Insurance insurance;
    protected FuelType fuelType;
    //private static int GlobalId = 0; //nu se va mai incrementa ID-ul la nivel de aplicatie, ci la nivel de BD
    //apoi se va prelua Id-ul din BD, la nivel de entitate
    protected int Id;
    boolean availability;
    ArrayList<Accident> accidents = new ArrayList<Accident>(); //fiecare vehicul are o lista de accidente
    static Scanner scanner;


    public Vehicle(String brand, String model, String color, int fabricationYear, double mileage,
                   double fuelConsumption, double capacity, FuelType fuelType, Scanner scanner) {
        //this.Id = GlobalId++;
        this.brand = brand;
        this.model = model;
        this.color = color;
        if(fabricationYear <= LocalDate.now().getYear()) {
            this.fabricationYear = fabricationYear;
        }
        else {
            throw new InvalidFabricationYear("Invalid fabrication year");
        }
        this.mileage = mileage;
        this.fuelType = fuelType;//shallow copy, daca se schimba pretul combustiilului
        //vreau ca acest lucru sa fie vizibil si in clasa Vehicle
        this.fuelConsumption = fuelConsumption;
        this.capacity = capacity;
        this.availability = true;
        this.scanner = scanner;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getFabricationYear() {
        return fabricationYear;
    }

    public double getMileage() {
        return mileage;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public int getId() {
        return Id;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }
    public void setFuelConsumption( double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setcapacity(double capacity) {
        this.capacity = capacity;
    }
    public double getCapacity() {
        return capacity;
    }

    public boolean checkInsurance(){
        return insurance != null && !insurance.getExpiryDate().isBefore(LocalDate.now());
    }
    //public abstract void renewInsurance(); Nu mai este nevoie sa o definesc aici ca si metoda abstracta,
    //e deja definita in Interfata VehicleInterface si implementata in clasele care extind Vehicle

    public void registerAccident() {
        /*
         * Id-ul accidentului este generat in baza de date
         * Metoda insert intoarce un obiect cu Id setat,
         * Acelasi Id ca in baza de date
         */
        double accidentPrice;

        System.out.println("How much did the accident cost to repair?");
        accidentPrice = scanner.nextDouble();
        scanner.nextLine();

        Accident localAccident = new Accident(LocalDate.now(), accidentPrice, this.Id);
        AccidentService service = new AccidentService();
        Accident insertedAccident = service.insertAccident(localAccident, this);

        accidents.add(insertedAccident);

        System.out.println(localAccident.toString());
    }

    public void displayAccidents() {
        for (Accident accident : accidents) {
            System.out.println(accident.toString());
        }
    }

    public Accident getAccident(int accidentId) { //returneaza un accident din lista de accidente a vehiculului
        for (Accident accident : accidents) {
            if (accident.getAccidentId() == accidentId) {
                return accident;
            }
        }
        return null;
    }

    public void addAccident(Accident accident) {
        accidents.add(accident);
    }

    public Insurance findInsurance() {
        return insurance;
    }

    public void setId(int id) {
        this.Id = id;
    }

    @Override
    public int compareTo(Vehicle obj) {

        if(mileage < obj.mileage) return -1; // This object is smaller than the other one
        if(mileage > obj.mileage) return 1;  // This object is larger than the other one
        return 0; // Both objects are the same
    }
}
