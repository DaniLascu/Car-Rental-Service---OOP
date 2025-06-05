import java.time.LocalDate;
import java.util.*;

public class Service {
    ArrayList<FuelType> fuelTypes = new ArrayList<FuelType>();
    ArrayList<Person> persons = new ArrayList<Person>();
    ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    HashMap<Vehicle,Mechanic> assignedMechanics = new HashMap<>();
    ArrayList<TechnicalInspection> inspections = new ArrayList<>();
    ArrayList<CarRental> rentalHistory = new ArrayList<>();
    Scanner scanner;

    //Servicii CRUD
    FuelTypeService fuelTypeService = new FuelTypeService();
    InsuranceService insuranceService = new InsuranceService();
    CarService carService = new CarService();
    MotorcycleService motorcycleService = new MotorcycleService();
    VanService vanService = new VanService();
    AccidentService accidentService = new AccidentService();
    MechanicService mechanicService = new MechanicService();
    TechnicalInspectionService technicalInspectionService = new TechnicalInspectionService();
    CustomerService customerService = new CustomerService();
    CarRentalService carRentalService = new CarRentalService();

    private static Service service = null;
    private Service(Scanner scanner) {
        //Se extrag tipurile de Combustibili din Baza de Date
        fuelTypeService.createTable();
        fuelTypes = fuelTypeService.getAll();

        insuranceService.createTable();
        carService.createTables();
        motorcycleService.createTables();
        vanService.createTables();
        accidentService.createTables();
        mechanicService.createTable();
        technicalInspectionService.createTables();
        customerService.createTable();
        carRentalService.createTables();

        this.scanner = scanner;
    }
    public static Service getInstance(Scanner scanner) {
        if (service == null) {
            service = new Service(scanner);
        }
        return service;
    }

    public void initializeVectors(){
        //extrag toate masinile din baza de date, in memorie
        ArrayList<Car> myCars = new ArrayList<>();
        myCars = carService.getAll(scanner, fuelTypes);
        vehicles.addAll(myCars);


        //extragem toate motcicletele din baza de date, in memorie
        ArrayList<Motorcycle> myMotorcycles = new ArrayList<>();
        myMotorcycles = motorcycleService.getAll(scanner, fuelTypes);
        vehicles.addAll(myMotorcycles);

        //extragem toate van-urile din baza de date, in memorie
        ArrayList<Van> myVans = new ArrayList<>();
        myVans = vanService.getAll(scanner, fuelTypes);
        vehicles.addAll(myVans);

        Collections.sort(vehicles);

        //pentru fiecare vehicul din vehicles, cautam aaccidentele din tabelul de accidente
        //apoi le introducem in lista din clasa
        for(Vehicle v : vehicles){
            ArrayList<Accident> myAccidents = accidentService.getAllAccidentsForVehicle(v);

            for(Accident a : myAccidents){
                v.addAccident(a);
            }
        }

        //adaugam toti mecanicii din baza de date, in lista de angajati
        ArrayList<Mechanic> localMechanics = mechanicService.getAllMechanics(scanner);
        persons.addAll(localMechanics);

        //adaugam toate inspectiile tehnice
        ArrayList<TechnicalInspection> inspectionsCars = technicalInspectionService.getAllFromCars(scanner, fuelTypes);
        ArrayList<TechnicalInspection> inspectionsVans = technicalInspectionService.getAllFromVans(scanner, fuelTypes);
        ArrayList<TechnicalInspection> inspectionsMotorcycles = technicalInspectionService.getAllFromMotorcycles(scanner, fuelTypes);


        inspections.addAll(inspectionsCars);
        inspections.addAll(inspectionsVans);
        inspections.addAll(inspectionsMotorcycles);


        Collections.sort(inspections);

        //Adaugam toti clientii din baza de date
        ArrayList<Customer> localCustomers = customerService.getAllCustomers(scanner);
        persons.addAll(localCustomers);

        //Adaugam istoricul de inchirieri
        ArrayList<CarRental> localCarRentalCars = carRentalService.getAllRentalsForCars(scanner, fuelTypes);
        ArrayList<CarRental> localCarRentalVans = carRentalService.getAllRentalsForVans(scanner, fuelTypes);
        ArrayList<CarRental> localCarRentalMotorcycles = carRentalService.getAllRentalsForMotorcycles(scanner, fuelTypes);

        rentalHistory.addAll(localCarRentalCars);
        rentalHistory.addAll(localCarRentalVans);
        rentalHistory.addAll(localCarRentalMotorcycles);

    }

    void searchFuelTypes(){
        for(FuelType fuelType : fuelTypes){
            System.out.println(fuelType);
        }
    }
    void addMechanic() {
        Mechanic newMechanic = new Mechanic(scanner);
        Mechanic insertedMechanic = mechanicService.insertMechanic(newMechanic); //trebuie sa primeasca id din bd

        persons.add(insertedMechanic);
    }
    void addCustomer() {
        try {
            Customer newClient = new Customer(scanner);
            Customer insertedCustomer = customerService.insertCustomer(newClient); //trebuie sa primeasca id din bd

            persons.add(insertedCustomer);
        }
        catch (InvalidDriverPermitException | InvalidFabricationYear e) {
            System.out.println(e.getMessage());
        }
    }
    void displayAllCustomers(){
        for(Person person : persons){
            if(person instanceof Customer){
                System.out.println(person);
            }
        }
    }
    void displayAllMechanics(){
        for(Person person : persons){
            if(person instanceof Mechanic){
                System.out.println(person);
            }
        }
    }
    void displayOneCustomer(){
        int Id;
        System.out.println("Customers' Ids:");
        for(Person person : persons){
            if(person instanceof Customer){
                System.out.println(((Customer) person).getID());
            }
        }
        System.out.println("Chose one of the Ids shown above:");
        Id=scanner.nextInt();
        scanner.nextLine();

        for(Person person : persons){
            if(person instanceof Customer && ((Customer) person).getID() == Id){
                System.out.println(((Customer) person));
            }
        }

    }
    void displayOneMechanic(){
        int Id;
        System.out.println("Employees' Ids:");
        for(Person person : persons){
            if(person instanceof Mechanic){
                System.out.println(((Mechanic) person).getID());
            }
        }
        System.out.println("Chose one of the Ids shown above:");
        Id=scanner.nextInt();
        scanner.nextLine();

        for(Person person : persons){
            if(person instanceof Mechanic && ((Mechanic) person).getID() == Id){
                System.out.println(((Mechanic) person));
            }
        }
    }

    void removeMechanic(){
        int Id;
        System.out.println("Employees' Ids:");
        for(Person person : persons){
            if(person instanceof Mechanic){
                System.out.println(((Mechanic) person).getID());
            }
        }
        System.out.println("Chose one of the Ids shown above:");
        Id=scanner.nextInt();
        scanner.nextLine();

        for(Person person : persons){
            if(person instanceof Mechanic && ((Mechanic) person).getID() == Id){
                mechanicService.deleteMechanicById(((Mechanic) person).getID());
            }
        }

        persons.removeIf(person -> person instanceof Mechanic && ((Mechanic) person).getID() == Id);
    }

    void removeCustomer(){
        int Id;
        System.out.println("Clients' Ids:");
        for(Person person : persons){
            if(person instanceof Customer){
                System.out.println(((Customer) person).getID());
            }
        }
        System.out.println("Chose one of the Ids shown above:");
        Id=scanner.nextInt();
        scanner.nextLine();

        for(Person person : persons){
            if(person instanceof Customer && ((Customer) person).getID() == Id){
                customerService.deleteCustomerById(((Customer) person).getID());
            }
        }

        persons.removeIf(person -> person instanceof Customer && ((Customer) person).getID() == Id);
    }

    void addCar(){
        System.out.println("press Enter");
        scanner.nextLine();
        System.out.println("Brand:");
        String brand = scanner.nextLine();

        System.out.println("Model:");
        String model = scanner.nextLine();

        System.out.println("Color:");
        String color = scanner.nextLine();

        System.out.println("Year:");
        int year = scanner.nextInt();
        scanner.nextLine();

        double mileage = 0;

        double leftLimit = 1D;
        double rightLimit = 10D;
        double fuelConsumption = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);

        System.out.println("Engine Capacity:");
        double engineCapacity = scanner.nextDouble();


        FuelType myFuelType = null;
        System.out.println("Chose Fuel Type:");
        System.out.println("1. Gasoline");
        System.out.println("2. Diesel");
        System.out.println("3. GPL");
        int option = 0;

        while(option != 1 && option != 2 && option != 3){
            option = scanner.nextInt();
            scanner.nextLine();
        }

        myFuelType = switch (option) {
            case 1 -> fuelTypes.getFirst();
            case 2 -> fuelTypes.get(1);
            case 3 -> fuelTypes.get(2);
            default -> myFuelType;
        };

        System.out.println("Number of seats:");
        int seats = scanner.nextInt();
        scanner.nextLine();

        try {
            Car myCar = new Car(brand, model, color, year, mileage, fuelConsumption, engineCapacity, myFuelType, seats, scanner);
            vehicles.add(myCar);
            carService.insert(myCar);
        }
        catch (InvalidFabricationYear e)
        {
            System.out.println(e.getMessage());
        }
        Collections.sort(vehicles);
    }

    void addVan(){
        System.out.println("press Enter");
        scanner.nextLine();
        System.out.println("Brand:");
        String brand = scanner.nextLine();

        System.out.println("Model:");
        String model = scanner.nextLine();

        System.out.println("Color:");
        String color = scanner.nextLine();

        System.out.println("Year:");
        int year = scanner.nextInt();
        scanner.nextLine();

        double mileage = 0;

        double leftLimit = 1D;
        double rightLimit = 10D;
        double fuelConsumption = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);

        System.out.println("Engine Capacity:");
        double engineCapacity = scanner.nextDouble();


        FuelType myFuelType = null;
        System.out.println("Chose Fuel Type:");
        System.out.println("1. Gasoline");
        System.out.println("2. Diesel");
        System.out.println("3. GPL");
        int option = 0;

        while(option != 1 && option != 2 && option != 3){
            option = scanner.nextInt();
            scanner.nextLine();
        }

        myFuelType = switch (option) {
            case 1 -> fuelTypes.get(0);
            case 2 -> fuelTypes.get(1);
            case 3 -> fuelTypes.get(2);
            default -> myFuelType;
        };

        System.out.println("Weight Capacity:");
        double weightCapacity = scanner.nextDouble();

        try {
            Van myVan = new Van(brand, model, color, year, mileage, fuelConsumption, engineCapacity, myFuelType, weightCapacity, scanner);

            vehicles.add(myVan);
            vanService.insert(myVan);
        }
        catch (InvalidFabricationYear e)
        {
            System.out.println(e.getMessage());
        }

        Collections.sort(vehicles);
    }

    void addMotorcycle(){
        System.out.println("press Enter");
        scanner.nextLine();
        System.out.println("Brand:");
        String brand = scanner.nextLine();

        System.out.println("Model:");
        String model = scanner.nextLine();

        System.out.println("Color:");
        String color = scanner.nextLine();

        System.out.println("Year:");
        int year = scanner.nextInt();
        scanner.nextLine();

        double mileage = 0;

        double leftLimit = 1D;
        double rightLimit = 10D;
        double fuelConsumption = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);

        System.out.println("Engine Capacity:");
        double engineCapacity = scanner.nextDouble();


        FuelType myFuelType = null;
        System.out.println("Chose Fuel Type:");
        System.out.println("1. Gasoline");
        System.out.println("2. Diesel");
        System.out.println("3. GPL");
        int option = 0;

        while(option != 1 && option != 2 && option != 3){
            option = scanner.nextInt();
            scanner.nextLine();
        }

        myFuelType = switch (option) {
            case 1 -> fuelTypes.get(0);
            case 2 -> fuelTypes.get(1);
            case 3 -> fuelTypes.get(2);
            default -> myFuelType;
        };

        System.out.println("Has Sidecar:");
        boolean sidecar = scanner.nextBoolean();

        MotorcycleType myMotorcycleType = null;
        System.out.println("Chose Motorcycle:");
        System.out.println("1. Cruiser");
        System.out.println("2. Sport");
        System.out.println("3. Touring");
        System.out.println("4. Standard");
        System.out.println("5. Dirt");

        option = 0;
        while(option != 1 && option != 2 && option != 3 && option != 4 && option != 5){
            option = scanner.nextInt();
            scanner.nextLine();
        }

        myMotorcycleType = switch (option){
            case 1 -> MotorcycleType.CRUISER;
            case 2 -> MotorcycleType.SPORT;
            case 3 -> MotorcycleType.TOURING;
            case 4 -> MotorcycleType.STANDARD;
            case 5 -> MotorcycleType.DIRT;
            default -> myMotorcycleType;
        };

        System.out.println("Has Storage:");
        boolean storage = scanner.nextBoolean();

        try {
            Motorcycle myMotorcycle = new Motorcycle(brand, model, color, year, mileage, fuelConsumption, engineCapacity, myFuelType, sidecar, myMotorcycleType, storage, scanner);
            vehicles.add(myMotorcycle);
            motorcycleService.insert(myMotorcycle);
        }
        catch (InvalidFabricationYear e){
            System.out.println(e.getMessage());
        }

        Collections.sort(vehicles);
    }

    public void displayAllCars(){
        for(Vehicle v : vehicles){
            if(v instanceof Car){
                System.out.println(((Car) v));
            }
        }
    }

    public void displayAllVans(){
        for(Vehicle v : vehicles){
            if(v instanceof Van){
                System.out.println(((Van) v));
            }
        }
    }

    public void displayAllMotorcycles(){
        for(Vehicle v : vehicles){
            if(v instanceof Motorcycle){
                System.out.println(((Motorcycle) v));
            }
        }
    }

    public void insureCar(){
        List<Car> uninsuredCars = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v instanceof Car car && !car.checkInsurance()) {
                uninsuredCars.add(car);
            }
        }

        if (uninsuredCars.isEmpty()) {
            System.out.println("There are no uninsured cars.");
            return;
        }

        System.out.println("All uninsured cars:");
        for (Car car : uninsuredCars) {
            System.out.println(car);
        }

        System.out.println("Choose the car (by ID) you want to insure:");
        int option = scanner.nextInt();
        scanner.nextLine();

        for (Car car : uninsuredCars) {
            if (car.getId() == option) {
                //masinile care intra in Lista de masini nu au asigurare valabile
                //daca car.findinsurance == null, inseamna ca acum s-a creat un obiect insurance pentru masina respectiva,
                //pentru prima oare, deci trbuie un inser in baza de date
                //daca nu e null, inseamna ca exista o inregistrare pentru insurace in baza de date, care trebuie doar
                //updatata
                //deoarece atributul insurance va fi merreu null, la insert, in entitatea Car
                //atunci cand il initializez in Enititate, prin car.renewInsurance, trebuie sa il introduc si in
                //inregistrarea din tabelul cars
                if(car.findInsurance() == null){
                    car.renewInsurance();
                    insuranceService.insert(car.getInsurance().getExpiryDate(), car.getInsurance().getPrice());
                    carService.updateInsurance(car.getId(), car.getInsurance());
                }
                else {
                    car.renewInsurance();
                    insuranceService.update(car.getInsurance().getId(), car.getInsurance().getExpiryDate(), car.getInsurance().getPrice());
                }

                System.out.println("Car insurance renewed!");
                return;
            }
        }

        System.out.println("Invalid ID selected.");
    }

    public void insureVan(){
        List<Van> uninsuredVans = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v instanceof Van van && !van.checkInsurance()) {
                uninsuredVans.add(van);
            }
        }

        if (uninsuredVans.isEmpty()) {
            System.out.println("There are no uninsured vans.");
            return;
        }

        System.out.println("All uninsured vans:");
        for (Van van : uninsuredVans) {
            System.out.println(van);
        }

        System.out.println("Choose the van (by ID) you want to insure:");
        int option = scanner.nextInt();
        scanner.nextLine();

        for (Van van : uninsuredVans) {
            if (van.getId() == option) {
                if(van.findInsurance() == null){
                    van.renewInsurance();
                    insuranceService.insert(van.getInsurance().getExpiryDate(), van.getInsurance().getPrice());
                    vanService.updateInsurance(van.getId(), van.getInsurance());
                }
                else {
                    van.renewInsurance();
                    insuranceService.update(van.getInsurance().getId(), van.getInsurance().getExpiryDate(), van.getInsurance().getPrice());
                }
                System.out.println("Van insurance renewed!");
                return;
            }
        }

        System.out.println("Invalid ID selected.");
    }

    public void insureMotorcycle(){
        List<Motorcycle> uninsuredBikes = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v instanceof Motorcycle bike && !bike.checkInsurance()) {
                uninsuredBikes.add(bike);
            }
        }

        if (uninsuredBikes.isEmpty()) {
            System.out.println("There are no uninsured motorcycles.");
            return;
        }

        System.out.println("All uninsured motorcycles:");
        for (Motorcycle bike : uninsuredBikes) {
            System.out.println(bike);
        }

        System.out.println("Choose the motorcycle (by ID) you want to insure:");
        int option = scanner.nextInt();
        scanner.nextLine();

        for (Motorcycle bike : uninsuredBikes) {
            if (bike.getId() == option) {

                if(bike.findInsurance() == null){
                    bike.renewInsurance();
                    insuranceService.insert(bike.getInsurance().getExpiryDate(), bike.getInsurance().getPrice());
                    motorcycleService.updateInsurance(bike.getId(), bike.getInsurance());
                }
                else {
                    bike.renewInsurance();
                    insuranceService.update(bike.getInsurance().getId(), bike.getInsurance().getExpiryDate(), bike.getInsurance().getPrice());
                }
                System.out.println("Motorcycle insurance renewed!");
                return;
            }
        }

        System.out.println("Invalid ID selected.");
    }


    public void renewInsurance(){
        int option  = 0;
        System.out.println("Choose The Type of Vehicle you want to Insure:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        option = scanner.nextInt();
        scanner.nextLine();
        while(option != 1 && option != 2 && option != 3){
            option = scanner.nextInt();
            scanner.nextLine();
        }
        switch(option){
            case 1:
                insureCar();
                break;
            case 2:
                insureVan();
                break;
            case 3:
                insureMotorcycle();
                break;
            default:
                break;
        }
    }

    private double updatePrice(){
        System.out.println("Enter the new price per Gallon:");

        double price = scanner.nextDouble();
        scanner.nextLine();

        if(price < 0){
            System.out.println("The price cannot be negative");
            System.out.println("Enter the new price per Gallon:");
        }
        while(price < 0){
            price = scanner.nextDouble();
            scanner.nextLine();
        }
        return price;
    }

    public void updateFuelPrice(){
        System.out.println("What Fuel Type do you want to update?:");
        System.out.println("1. Gasoline");
        System.out.println("2. Diesel");
        System.out.println("3. GPL");

        int option = scanner.nextInt();
        scanner.nextLine();
        while(option != 1 && option != 2 && option != 3){
            option = scanner.nextInt();
            scanner.nextLine();
        }
        switch(option){
            case 1:
                System.out.println("Gasoline price: " + fuelTypes.getFirst().getPricePerGallon());
                fuelTypes.getFirst().setPricePerGallon(updatePrice());
                fuelTypeService.updatePricePerGallon(fuelTypes.getFirst().getFuelType(), fuelTypes.getFirst().getPricePerGallon());
                break;
            case 2:
                System.out.println("Diesel price: " + fuelTypes.get(1).getPricePerGallon());
                fuelTypes.get(1).setPricePerGallon(updatePrice());
                fuelTypeService.updatePricePerGallon(fuelTypes.get(1).getFuelType(), fuelTypes.get(1).getPricePerGallon());
                break;
            case 3:
                System.out.println("GPL price: " + fuelTypes.get(2).getPricePerGallon());
                fuelTypes.get(2).setPricePerGallon(updatePrice());
                fuelTypeService.updatePricePerGallon(fuelTypes.get(2).getFuelType(), fuelTypes.get(2).getPricePerGallon());
                break;
            default:
                break;
        }
    }

    public void assignVehicleToMechanic(){
        List<Vehicle> unassignedVehicles = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (!assignedMechanics.containsKey(v)) {
                unassignedVehicles.add(v);
            }
        }

        if (unassignedVehicles.isEmpty()) {
            System.out.println("All vehicles already have assigned mechanics.");
            return;
        }

        System.out.println("Chose the type of vehicle:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");
        int option = scanner.nextInt();
        scanner.nextLine();

        while(option != 1 && option != 2 && option != 3){
            System.out.println("Choose the type of vehicle you want to assign:");
            option = scanner.nextInt();
            scanner.nextLine();
        }

        List<Mechanic> allMechanics = new ArrayList<>();
        for (Person person : persons) {
            if (person instanceof Mechanic) {
                allMechanics.add((Mechanic) person);
            }
        }

        if(option == 1){
            System.out.println("Cars without assigned mechanics:");
            for (Vehicle v : unassignedVehicles) {
                if(v instanceof Car) System.out.println(v);
            }

            System.out.println("Choose the vehicle (by ID) you want to assign a mechanic to:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : unassignedVehicles) {
                if (v.getId() == vehicleId && v instanceof Car) {
                    selectedVehicle = v;
                    break;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("Invalid car ID.");
                return;
            }



            if (allMechanics.isEmpty()) {
                System.out.println("There are no mechanics available.");
                return;
            }

            System.out.println("Available mechanics:");
            for (Mechanic m : allMechanics) {
                System.out.println(m);
            }

            System.out.println("Choose the mechanic (by ID) to assign to the vehicle:");
            int mechanicId = scanner.nextInt();
            scanner.nextLine();

            Mechanic selectedMechanic = null;
            for (Mechanic m : allMechanics) {
                if (m.getID() == mechanicId) {
                    selectedMechanic = m;
                    break;
                }
            }

            if (selectedMechanic == null) {
                System.out.println("Invalid mechanic ID.");
                return;
            }

            assignedMechanics.put(selectedVehicle, selectedMechanic);
            System.out.println("Mechanic assigned successfully!");
        }
        if(option == 2){
            System.out.println("Vans without assigned mechanics:");
            for (Vehicle v : unassignedVehicles) {
                if(v instanceof Van) System.out.println(v);
            }

            System.out.println("Choose the vehicle (by ID) you want to assign a mechanic to:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : unassignedVehicles) {
                if (v.getId() == vehicleId && v instanceof Van) {
                    selectedVehicle = v;
                    break;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("Invalid car ID.");
                return;
            }

            if (allMechanics.isEmpty()) {
                System.out.println("There are no mechanics available.");
                return;
            }

            System.out.println("Available mechanics:");
            for (Mechanic m : allMechanics) {
                System.out.println(m);
            }

            System.out.println("Choose the mechanic (by ID) to assign to the vehicle:");
            int mechanicId = scanner.nextInt();
            scanner.nextLine();

            Mechanic selectedMechanic = null;
            for (Mechanic m : allMechanics) {
                if (m.getID() == mechanicId) {
                    selectedMechanic = m;
                    break;
                }
            }

            if (selectedMechanic == null) {
                System.out.println("Invalid mechanic ID.");
                return;
            }

            assignedMechanics.put(selectedVehicle, selectedMechanic);
            System.out.println("Mechanic assigned successfully!");
        }
        if(option == 3){
            System.out.println("Motorcycles without assigned mechanics:");
            for (Vehicle v : unassignedVehicles) {
                if(v instanceof Motorcycle) System.out.println(v);
            }

            System.out.println("Choose the vehicle (by ID) you want to assign a mechanic to:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : unassignedVehicles) {
                if (v.getId() == vehicleId && v instanceof Motorcycle) {
                    selectedVehicle = v;
                    break;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("Invalid car ID.");
                return;
            }



            if (allMechanics.isEmpty()) {
                System.out.println("There are no mechanics available.");
                return;
            }

            System.out.println("Available mechanics:");
            for (Mechanic m : allMechanics) {
                System.out.println(m);
            }

            System.out.println("Choose the mechanic (by ID) to assign to the vehicle:");
            int mechanicId = scanner.nextInt();
            scanner.nextLine();

            Mechanic selectedMechanic = null;
            for (Mechanic m : allMechanics) {
                if (m.getID() == mechanicId) {
                    selectedMechanic = m;
                    break;
                }
            }

            if (selectedMechanic == null) {
                System.out.println("Invalid mechanic ID.");
                return;
            }

            assignedMechanics.put(selectedVehicle, selectedMechanic);
            System.out.println("Mechanic assigned successfully!");
        }

    }

    public void findMechanicAssignedToVehicle() {
        if (vehicles.isEmpty()) {
            System.out.println("There are no vehicles in the system.");
            return;
        }

        System.out.println("Choose the vehicle type:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        int option = scanner.nextInt();
        scanner.nextLine();

        while(option != 1 && option != 2 && option != 3){
            System.out.println("Choose the vehicle type:");
            option = scanner.nextInt();
            scanner.nextLine();
        }

        if(option == 1){
            System.out.println("Available Cars:");
            for (Vehicle v : vehicles) {
                if(v instanceof Car) System.out.println(v);
            }

            System.out.println("Enter the ID of the Car you want to check:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : vehicles) {
                if (v.getId() == vehicleId && v instanceof Car) {
                    selectedVehicle = v;
                    break;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("No vehicle found with the given ID.");
                return;
            }

            Mechanic assigned = assignedMechanics.get(selectedVehicle);
            if (assigned != null) {
                System.out.println("Mechanic assigned to this vehicle:");
                System.out.println(assigned);
            } else {
                System.out.println("No mechanic is currently assigned to this vehicle.");
            }
        }
        if(option == 2){
            System.out.println("Available Vans:");
            for (Vehicle v : vehicles) {
                if(v instanceof Van) System.out.println(v);
            }

            System.out.println("Enter the ID of the Van you want to check:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : vehicles) {
                if (v.getId() == vehicleId && v instanceof Van) {
                    selectedVehicle = v;
                    break;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("No vehicle found with the given ID.");
                return;
            }

            Mechanic assigned = assignedMechanics.get(selectedVehicle);
            if (assigned != null) {
                System.out.println("Mechanic assigned to this vehicle:");
                System.out.println(assigned);
            } else {
                System.out.println("No mechanic is currently assigned to this vehicle.");
            }
        }
        if(option == 3){
            System.out.println("Available Motorcycles:");
            for (Vehicle v : vehicles) {
                if(v instanceof Motorcycle) System.out.println(v);
            }

            System.out.println("Enter the ID of the Motorcycle you want to check:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : vehicles) {
                if (v.getId() == vehicleId && v instanceof Motorcycle) {
                    selectedVehicle = v;
                    break;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("No vehicle found with the given ID.");
                return;
            }

            Mechanic assigned = assignedMechanics.get(selectedVehicle);
            if (assigned != null) {
                System.out.println("Mechanic assigned to this vehicle:");
                System.out.println(assigned);
            } else {
                System.out.println("No mechanic is currently assigned to this vehicle.");
            }
        }
    }

    public void removeVehicle(){
        if(vehicles.isEmpty()){
            System.out.println("There are no vehicles in the system.");
        }

        System.out.println("Enter what type of vehicle you want to remove:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        int choice = scanner.nextInt();
        scanner.nextLine();

        while(choice != 1 && choice != 2 && choice != 3){
            System.out.println("Please enter a valid choice:");
            choice = scanner.nextInt();
            scanner.nextLine();
        }

        if(choice == 1){
            System.out.println("Available cars:");
            for (Vehicle v : vehicles) {
                if(v instanceof Car){
                    System.out.println(v);
                }
            }

            System.out.println("Enter the ID of the vehicle you want to remove:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : vehicles) {
                if (v.getId() == vehicleId && v instanceof Car) {
                    selectedVehicle = v;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("No car found with the given ID.");
            }
            else {
                if (selectedVehicle.getInsurance() != null) {
                    insuranceService.delete(selectedVehicle.getInsurance().getId());
                }
                carService.delete(selectedVehicle.getId());
                vehicles.remove(selectedVehicle);
            }

        }

        if(choice == 2){
            System.out.println("Available vans:");
            for (Vehicle v : vehicles) {
                if(v instanceof Van){
                    System.out.println(v);
                }
            }

            System.out.println("Enter the ID of the vehicle you want to remove:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : vehicles) {
                if (v.getId() == vehicleId && v instanceof Van) {
                    selectedVehicle = v;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("No car found with the given ID.");
            }
            else {
                if (selectedVehicle.getInsurance() != null) {
                    insuranceService.delete(selectedVehicle.getInsurance().getId());
                }
                vanService.delete(selectedVehicle.getId());
                vehicles.remove(selectedVehicle);
            }

        }

        if(choice == 3){
            System.out.println("Available vans:");
            for (Vehicle v : vehicles) {
                if(v instanceof Motorcycle){
                    System.out.println(v);
                }
            }

            System.out.println("Enter the ID of the vehicle you want to remove:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for (Vehicle v : vehicles) {
                if (v.getId() == vehicleId && v instanceof Motorcycle) {
                    selectedVehicle = v;
                }
            }

            if (selectedVehicle == null) {
                System.out.println("No car found with the given ID.");
            }
            else {
                if (selectedVehicle.getInsurance() != null) {
                    insuranceService.delete(selectedVehicle.getInsurance().getId());
                }
                motorcycleService.delete(selectedVehicle.getId());
                vehicles.remove(selectedVehicle);
            }

        }

        Collections.sort(vehicles);
    }

    public void technicalInspection() {
        if (assignedMechanics.isEmpty()) {
            System.out.println("There are no mechanics assigned to any vehicle.");
            System.out.println("You need to assign mechanics first.");
            return;
        }

        System.out.println("Enter the type of vehicle you want to inspect:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        int choice = scanner.nextInt();
        scanner.nextLine();

        while(choice != 1 && choice != 2 && choice != 3){
            System.out.println("Please enter a valid choice:");
            choice = scanner.nextInt();
            scanner.nextLine();
        }

        int vehicleId;
        Mechanic assignedMechanic = null;
        Vehicle selectedVehicle = null;

        switch (choice) {
            case 1:
                System.out.println("Available Cars with assigned mechanics:");
                for (Map.Entry<Vehicle, Mechanic> entry : assignedMechanics.entrySet()) {
                    if(entry.getKey() instanceof Car) System.out.println(entry.getKey());
                }

                System.out.println("Enter the ID of the vehicle you want to inspect:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                selectedVehicle = null;
                assignedMechanic = null;

                for (Map.Entry<Vehicle, Mechanic> entry : assignedMechanics.entrySet()) {
                    if (entry.getKey().getId() == vehicleId && entry.getKey() instanceof Car) {
                        selectedVehicle = entry.getKey();
                        assignedMechanic = entry.getValue();
                        break;
                    }
                }

                if (selectedVehicle == null) {
                    System.out.println("No vehicle found with that ID.");
                    return;
                }

                TechnicalInspection localinspection = new TechnicalInspection(selectedVehicle, assignedMechanic);
                TechnicalInspection insertedinspection = technicalInspectionService.insert(localinspection, selectedVehicle, assignedMechanic);
                inspections.add(insertedinspection);

                System.out.println("Technical inspection completed and recorded.");
                break;
            case 2:
                System.out.println("Available Vans with assigned mechanics:");
                for (Map.Entry<Vehicle, Mechanic> entry : assignedMechanics.entrySet()) {
                    if(entry.getKey() instanceof Van) System.out.println(entry.getKey());
                }

                System.out.println("Enter the ID of the vehicle you want to inspect:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                selectedVehicle = null;
                assignedMechanic = null;

                for (Map.Entry<Vehicle, Mechanic> entry : assignedMechanics.entrySet()) {
                    if (entry.getKey().getId() == vehicleId && entry.getKey() instanceof Van) {
                        selectedVehicle = entry.getKey();
                        assignedMechanic = entry.getValue();
                        break;
                    }
                }

                if (selectedVehicle == null) {
                    System.out.println("No vehicle found with that ID.");
                    return;
                }

                localinspection = new TechnicalInspection(selectedVehicle, assignedMechanic);
                insertedinspection = technicalInspectionService.insert(localinspection, selectedVehicle, assignedMechanic);
                inspections.add(insertedinspection);

                System.out.println("Technical inspection completed and recorded.");
                break;

            case 3:
                System.out.println("Available Motorcycles with assigned mechanics:");
                for (Map.Entry<Vehicle, Mechanic> entry : assignedMechanics.entrySet()) {
                    if(entry.getKey() instanceof Motorcycle) System.out.println(entry.getKey());
                }

                System.out.println("Enter the ID of the vehicle you want to inspect:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                selectedVehicle = null;
                assignedMechanic = null;

                for (Map.Entry<Vehicle, Mechanic> entry : assignedMechanics.entrySet()) {
                    if (entry.getKey().getId() == vehicleId && entry.getKey() instanceof Motorcycle) {
                        selectedVehicle = entry.getKey();
                        assignedMechanic = entry.getValue();
                        break;
                    }
                }

                if (selectedVehicle == null) {
                    System.out.println("No vehicle found with that ID.");
                    return;
                }

                localinspection = new TechnicalInspection(selectedVehicle, assignedMechanic);
                insertedinspection = technicalInspectionService.insert(localinspection, selectedVehicle, assignedMechanic);
                inspections.add(insertedinspection);

                System.out.println("Technical inspection completed and recorded.");
                break;
        }

        Collections.sort(inspections);
    }

    public void checkTechnicalInspections(){
        if(inspections.isEmpty()){
            System.out.println("No Vehicle has had a technical inspection.");
            return;
        }

        System.out.println("Chose the type of vehicle:");
        System.out.println("1.Car");
        System.out.println("2.Van");
        System.out.println("3.Motorcycle");

        int choice = scanner.nextInt();
        scanner.nextLine();

        while (choice != 1 && choice != 2 && choice != 3){
            System.out.println("Enter a valid choice");
            choice = scanner.nextInt();
            scanner.nextLine();
        }

        List<TechnicalInspection> technicalInspections = new ArrayList<>();//inspections of the selected vehicle

        if(choice == 1){
            for(Vehicle v : vehicles){
                if(v instanceof Car) System.out.println(v);
            }
            System.out.println("Enter the ID of the Car you want to check:");

            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for(Vehicle v : vehicles){
                if (v.getId() == vehicleId && v instanceof Car) {
                    selectedVehicle = v;
                }
            }

            if(selectedVehicle == null){
                System.out.println("There is no Cars with the given ID.");
                return;
            }

            for(TechnicalInspection inspection : inspections){
                if(inspection.getVehicle().getId() == selectedVehicle.getId() && inspection.getVehicle() instanceof Car){
                    technicalInspections.add(inspection);
                }
            }

            if(technicalInspections.isEmpty()){
                System.out.println("No technical inspection found for a Car with that ID.");
            }
            else{
                for(TechnicalInspection inspection : technicalInspections){
                    System.out.println(inspection);
                }
            }
        }
        else if(choice == 2){
            for(Vehicle v : vehicles){
                if(v instanceof Van) System.out.println(v);
            }
            System.out.println("Enter the ID of the Van you want to check:");

            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for(Vehicle v : vehicles){
                if (v.getId() == vehicleId && v instanceof Van) {
                    selectedVehicle = v;
                }
            }

            if(selectedVehicle == null){
                System.out.println("There is no Van with the given ID.");
                return;
            }

            for(TechnicalInspection inspection : inspections){
                if(inspection.getVehicle().getId() == selectedVehicle.getId() && inspection.getVehicle() instanceof Van){
                    technicalInspections.add(inspection);
                }
            }

            if(technicalInspections.isEmpty()){
                System.out.println("No technical inspection found for a Van with that ID.");
            }
            else{
                for(TechnicalInspection inspection : technicalInspections){
                    System.out.println(inspection);
                }
            }
        }
        else {
            for(Vehicle v : vehicles){
                if(v instanceof Motorcycle) System.out.println(v);
            }
            System.out.println("Enter the ID of the Motorcycle you want to check:");

            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            Vehicle selectedVehicle = null;
            for(Vehicle v : vehicles){
                if (v.getId() == vehicleId && v instanceof Motorcycle) {
                    selectedVehicle = v;
                }
            }

            if(selectedVehicle == null){
                System.out.println("There is no Motorcycles with the given ID.");
                return;
            }

            for(TechnicalInspection inspection : inspections){
                if(inspection.getVehicle().getId() == selectedVehicle.getId() && inspection.getVehicle() instanceof Motorcycle){
                    technicalInspections.add(inspection);
                }
            }

            if(technicalInspections.isEmpty()){
                System.out.println("No technical inspection found for a Motor with that ID.");
            }
            else{
                for(TechnicalInspection inspection : technicalInspections){
                    System.out.println(inspection);
                }
            }
        }

    }

    public void rentCar(){
        if(vehicles.isEmpty()){
            System.out.println("There are no vehicles in the system.");
            return;
        }
        if(persons.isEmpty()){
            System.out.println("There are no clients in the system.");
        }

        System.out.println("Enter your Client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        Person selectedClient = null;
        for(Person p : persons){
            if(p instanceof Customer && ((Customer) p).getID() == clientId){
                selectedClient = p;
            }
        }

        if(selectedClient == null){
            System.out.println("No client found with the given ID.");
            return;
        }

        System.out.println("Chose the type of vehicle you want to rent:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        int option = scanner.nextInt();
        scanner.nextLine();

        while(option != 1 && option != 2 && option != 3){
            System.out.println("Please enter the type of vehicle you want to rent:");
            System.out.println("1. Car");
            System.out.println("2. Van");
            System.out.println("3. Motorcycle");
        }

        Vehicle selectedVehicle = null;
        int vehicleId;

        switch(option){
            case 1:
                System.out.println("Chose the car you want to rent:");
                for(Vehicle v : vehicles){
                    if(v instanceof Car) System.out.println(v);
                }
                System.out.println("Enter the ID of the vehicle you want to rent:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Car) {
                        selectedVehicle = v;
                    }
                }
                break;
            case 2:
                System.out.println("Chose the van you want to rent:");
                for(Vehicle v : vehicles){
                    if(v instanceof Van) System.out.println(v);
                }
                System.out.println("Enter the ID of the vehicle you want to rent:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Van) {
                        selectedVehicle = v;
                    }
                }
                break;
            case 3:
                System.out.println("Chose the motorcycle you want to rent:");
                for(Vehicle v : vehicles){
                    if(v instanceof Motorcycle) System.out.println(v);
                }
                System.out.println("Enter the ID of the vehicle you want to rent:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Motorcycle) {
                        selectedVehicle = v;
                    }
                }
                break;
        }

        if(selectedVehicle == null){
            System.out.println("No vehicle found with the given ID.");
            return;
        }

        TechnicalInspection technicalInspection = null;
        //se cauta o inspectie in lista de inspectii tehnice
        //sa fie una valida
        for(TechnicalInspection inspection : inspections){
            if(inspection.getVehicle().getId() == selectedVehicle.getId() && inspection.isValidInspection()){
                technicalInspection = inspection;
            }
        }

        if(technicalInspection == null){
            System.out.println("The vehicle you want to rent doesn't have a valid technical inspection.");
            return;
        }

        if(!selectedVehicle.checkInsurance()/*selectedVehicle.getInsurance() == null || selectedVehicle.getInsurance().getExpiryDate().isBefore(LocalDate.now())*/){
            System.out.println("The vehicle you want to rent doesn't have insurance.");
            return;
        }

        if (!selectedVehicle.availability) {
            System.out.println("The vehicle you want to rent is not available.");
            return;
        }

        System.out.println("Enter rental end date (YYYY-MM-DD):");
        String endDateStr = scanner.nextLine();
        LocalDate endDate;

        try {
            endDate = LocalDate.parse(endDateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        if (endDate.isBefore(startDate)) {
            System.out.println("Rental end date must be after today's date.");
            return;
        }

        CarRental localRental = new CarRental(selectedVehicle, (Customer) selectedClient);
        localRental.setEndDate(endDate);
        CarRental insertedRental = carRentalService.insert(localRental,selectedVehicle, (Customer) selectedClient);
        rentalHistory.add(insertedRental);

        selectedVehicle.setAvailability(false);
        if(selectedVehicle instanceof Car){
            carService.updateAvailability(selectedVehicle.getId(),false);
        }
        else if(selectedVehicle instanceof Van){
            vanService.updateAvailability(selectedVehicle.getId(),false);
        }
        else {
            motorcycleService.updateAvailability(selectedVehicle.getId(),false);
        }

        System.out.println("Successfully rent the car.!");
        System.out.println(insertedRental);
    }

    public void displayRentalsForCustomer() {
        if (rentalHistory.isEmpty()) {
            System.out.println("No rental found in the system.");
            return;
        }

        System.out.println("Enter your client ID:");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        Customer selectedCustomer = null;
        for (Person p : persons) {
            if (p instanceof Customer && ((Customer) p).getID() == clientId) {
                selectedCustomer = (Customer) p;
                break;
            }
        }

        if (selectedCustomer == null) {
            System.out.println("The client with this ID does not exist.");
            return;
        }

        List<CarRental> customerRentals = new ArrayList<>();
        for (CarRental rental : rentalHistory) {
            if (rental.getCustomer().getID() == selectedCustomer.getID()) {
                customerRentals.add(rental);
            }
        }

        if (customerRentals.isEmpty()) {
            System.out.println("The client doesn't have any customer rentals.");
        } else {
            System.out.println("Client's rental list:");
            for (CarRental rental : customerRentals) {
                System.out.println(rental);
            }
        }
    }

    public void updateRentalStatuses() {
        LocalDate today = LocalDate.now();

        for (CarRental rental : rentalHistory) {
            Vehicle vehicle = rental.getVehicle();
            LocalDate endDate = rental.getEndDate();

            if (endDate != null && !vehicle.availability && !endDate.isAfter(today)) {
                vehicle.setAvailability(true);
                if(vehicle instanceof Car){
                    carService.updateAvailability(vehicle.getId(),true);
                }
                else if(vehicle instanceof Van){
                    vanService.updateAvailability(vehicle.getId(),true);
                }
                else if(vehicle instanceof Motorcycle){
                    motorcycleService.updateAvailability(vehicle.getId(),true);
                }
                System.out.println("Vehicle with ID " + vehicle.getId() + " is now available.");
            }
        }
    }

    public void registerAccident() {
        System.out.println("Enter the vehicle type that had an accident:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        int option = scanner.nextInt();
        scanner.nextLine();

        while(option != 1 && option != 2 && option != 3) {
            System.out.println("Enter a viable option:");
            option = scanner.nextInt();
            scanner.nextLine();
        }

        Vehicle selectedVehicle = null;
        int vehicleId;

        switch(option){
            case 1:
                System.out.println("Enter the ID of the Car you want to register the accident for:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Car) {
                        selectedVehicle = v;
                    }
                }
                if(selectedVehicle == null){
                    System.out.println("No car found with the given ID.");
                }
                else{
                    selectedVehicle.registerAccident();
                }
                break;
            case 2:
                System.out.println("Enter the ID of the Van you want to register the accident for:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Van) {
                        selectedVehicle = v;
                    }
                }
                if(selectedVehicle == null){
                    System.out.println("No Van found with the given ID.");
                }
                else{
                    selectedVehicle.registerAccident();
                }
                break;
            case 3:
                System.out.println("Enter the ID of the Motorcycle you want to register the accident for:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Motorcycle) {
                        selectedVehicle = v;
                    }
                }
                if(selectedVehicle == null){
                    System.out.println("No car found with the given ID.");
                }
                else{
                    selectedVehicle.registerAccident();
                }
                break;
        }

    }

    public void displayAccidents() {
        System.out.println("Enter the vehicle type:");
        System.out.println("1. Car");
        System.out.println("2. Van");
        System.out.println("3. Motorcycle");

        int option = scanner.nextInt();
        scanner.nextLine();

        while(option != 1 && option != 2 && option != 3) {
            System.out.println("Enter a viable option:");
            option = scanner.nextInt();
            scanner.nextLine();
        }

        Vehicle selectedVehicle = null;
        int vehicleId;

        switch(option){
            case 1:
                System.out.println("Enter the ID of the Car you want to display the accidents for:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Car) {
                        selectedVehicle = v;
                    }
                }
                if(selectedVehicle == null){
                    System.out.println("No car found with the given ID.");
                }
                else{
                    selectedVehicle.displayAccidents();
                }
                break;
            case 2:
                System.out.println("Enter the ID of the Van you want to display the accidents for:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Van) {
                        selectedVehicle = v;
                    }
                }
                if(selectedVehicle == null){
                    System.out.println("No Van found with the given ID.");
                }
                else{
                    selectedVehicle.displayAccidents();
                }
                break;
            case 3:
                System.out.println("Enter the ID of the Motorcycle you want to display the accidents for:");
                vehicleId = scanner.nextInt();
                scanner.nextLine();

                for(Vehicle v : vehicles){
                    if (v.getId() == vehicleId && v instanceof Motorcycle) {
                        selectedVehicle = v;
                    }
                }
                if(selectedVehicle == null){
                    System.out.println("No car found with the given ID.");
                }
                else{
                    selectedVehicle.displayAccidents();
                }
                break;
        }
    }

}
