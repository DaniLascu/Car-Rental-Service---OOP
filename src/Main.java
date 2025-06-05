import java.sql.Driver;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static Scanner scanner = new Scanner(System.in);
    private static Service myService = Service.getInstance(scanner);
    public static void main(String[] args) {
        myService.initializeVectors();
        boolean var = true;
        while(var){
            myService.updateRentalStatuses();
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n------------------------\n");
            System.out.println("1.Check available Fuel Types: ");
            System.out.println("2.Hire a new Mechanic: ");
            System.out.println("3.Add a new Customer: ");
            System.out.println("4.Display all Employees: ");
            System.out.println("5.Display all Customers: ");
            System.out.println("6.Display one Employee: ");
            System.out.println("7.Display one Customer: ");
            System.out.println("8.Remove an Employee: ");
            System.out.println("9.Remove a Customer: ");
            System.out.println("10.Add a new Car: ");
            System.out.println("11.Add a new Van: ");
            System.out.println("12.Add a new Motorcycle: ");
            System.out.println("13.Display all Cars: ");
            System.out.println("14.Display all Vans: ");
            System.out.println("15.Display all Motorcycles: ");
            System.out.println("16.Insure a Vehicle: ");
            System.out.println("17.Change Fuel Price: ");
            System.out.println("18.Assign a Mechanic to a Vehicle: ");
            System.out.println("19.Find Mechanic assigned to a vehicle: ");
            System.out.println("20.Remove a Vehicle: ");
            System.out.println("21.Perform Technical Inspection: ");
            System.out.println("22.Check vehicle for Technical Inspection: ");
            System.out.println("23.Rent a Vehicle: ");
            System.out.println("24.Display Rental Information: ");
            System.out.println("25.Register an Accident: ");
            System.out.println("26.Display Accident History: ");
            System.out.println("27.Exit ");
            System.out.println("\n------------------------\n");
            int choice = scanner.nextInt();
            switch(choice) {
                case 1:
                    myService.searchFuelTypes();
                    AuditService.getInstance().logAction("searchFuelTypes");
                    break;
                case 2:
                    myService.addMechanic();
                    AuditService.getInstance().logAction("addMechanic");
                    break;
                case 3:
                    myService.addCustomer();
                    AuditService.getInstance().logAction("addCustomer");
                    break;
                case 4:
                    myService.displayAllMechanics();
                    AuditService.getInstance().logAction("displayAllMechanics");
                    break;
                case 5:
                    myService.displayAllCustomers();
                    AuditService.getInstance().logAction("displayAllCustomers");
                    break;
                case 6:
                    myService.displayOneMechanic();
                    AuditService.getInstance().logAction("displayOneMechanic");
                    break;
                case 7:
                    myService.displayOneCustomer();
                    AuditService.getInstance().logAction("displayOneCustomer");
                    break;
                case 8:
                    myService.removeMechanic();
                    AuditService.getInstance().logAction("removeMechanic");
                    break;
                case 9:
                    myService.removeCustomer();
                    AuditService.getInstance().logAction("removeCustomer");
                    break;
                case 10:
                    myService.addCar();
                    AuditService.getInstance().logAction("addCar");
                    break;
                case 11:
                    myService.addVan();
                    AuditService.getInstance().logAction("addVan");
                    break;
                case 12:
                    myService.addMotorcycle();
                    AuditService.getInstance().logAction("addMotorcycle");
                    break;
                 case 13:
                     myService.displayAllCars();
                     AuditService.getInstance().logAction("displayAllCars");
                     break;
                case 14:
                     myService.displayAllVans();
                     AuditService.getInstance().logAction("displayAllVans");
                     break;
                case 15:
                      myService.displayAllMotorcycles();
                      AuditService.getInstance().logAction("displayAllMotorcycles");
                      break;
                case 16:
                    myService.renewInsurance();
                    AuditService.getInstance().logAction("renewInsurance");
                    break;
                case 17:
                    myService.updateFuelPrice();
                    AuditService.getInstance().logAction("updateFuelPrice");
                    break;
                case 18:
                    myService.assignVehicleToMechanic();
                    AuditService.getInstance().logAction("assignVehicleToMechanic");
                    break;
                case 19:
                    myService.findMechanicAssignedToVehicle();
                    AuditService.getInstance().logAction("findMechanicAssignedToVehicle");
                    break;
                case 20:
                    myService.removeVehicle();
                    AuditService.getInstance().logAction("removeVehicle");
                    break;
                case 21:
                    myService.technicalInspection();
                    AuditService.getInstance().logAction("technicalInspection");
                    break;
                case 22:
                    myService.checkTechnicalInspections();
                    AuditService.getInstance().logAction("checkTechnicalInspections");
                    break;
                case 23:
                    myService.rentCar();
                    AuditService.getInstance().logAction("rentCar");
                    break;
                case 24:
                    myService.displayRentalsForCustomer();
                    AuditService.getInstance().logAction("displayRentalsForCustomer");
                    break;
                case 25:
                    myService.registerAccident();
                    AuditService.getInstance().logAction("registerAccident");
                    break;
                case 26:
                    myService.displayAccidents();
                    AuditService.getInstance().logAction("displayAccidents");
                    break;
                case 27:
                    var = false;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}