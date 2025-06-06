This is a Java Car Rental System application. This project showcases the use of OOP concepts such as inheritance, encapsulation, polymorphism and abstraction. It simulates the workflow of a car rental firm.
It contains a Vehicle Interface and a Vehicle abstract Class, that contains the default attributes of any vehicle, which is extended by other 3 classes: Car, Van and Motorcycle. Each Vehicle has a type of fuel, an insurance
and a list of accidents, which are all implemented as separate Classes. The system allows the user to register new mechanics, and new customers. Mechanics and Customer are implemented as separate Classes, both of them
extend an abstract class Person. Mechanics and Vehicles interact through the TechnicalInspection Class. Customers and Vehicles interact through the CarRental Class. The system allows multiple interactions between the 
entities. The user of the application can ADD and DELETE mechanics, customers and vehicles. They can also assign a mechanic for a car, program a technical inspection, performed by the assigned mechanic to a car and insure
a car. The app user can also rent a Vehicle for a Client. A Vehicle can only be rented if it has an Insurance and a valid TechnicalInspection. I also implemented some custom exceptions: when adding a new client, their driver's permit
must belong to a valid category of the Romanian driving license system. Classes Vehicle and Insurance Implement Comparable class. The system stores on the app level, collections for Person, FuelType, CarRental, Vehicle and Insurance, the last 2 being sorted collections.

I also implemented the persistency  of the data, using a database system, specifically MySQL. Each class in the system has a database table associated to it and a Service file that contains the CRUP operations. When starting
the application, the data is fetched from the database. Also when creating and deleting an object, a corresponding database record is created in the corresponding table.

Flux example:<br>
-The user adds a new Mechanic;<br>
-The user adds a new Vehicle and selects its type (Car, Van, or Motorcycle);<br>
-A new Customer is registered;<br>
-Customer X wants to rent Vehicle Y;<br>
-A Mechanic is assigned to Vehicle Y;<br>
-The mechanic performs a Technical Inspection on Vehicle Y;<br>
-Vehicle Y is Insured;<br>
-Customer X rents Vehicle Y — the rental period is specified, the vehicle becomes unavailable both at the application and database level;

![ER_Diagram_OOP2](https://github.com/user-attachments/assets/e48963c3-44eb-409f-9996-147e36ed7b3c)
