**SmartPark - Parking Management System**

SmartPark is a RESTful API service designed to optimize urban parking space usage and streamline vehicle navigation. 
It provides core functionality for managing parking lots, registering vehicles, and handling real-time check-in/check-out processes with automated occupancy tracking


Technologies:
  - Java 17.0.19
  - Spring boot 4.1.0
  - Spring Data JPA 4.1.0
  - H2 Database 2.4.240
  - Lombok 1.18.46
  - Maven



## Features ##
1. Parking Lot registration.
   - Exceeding lot ID characters validation.
     
2. Vehicle registration
   - Input format validation (license plate and owner name).
   - Vehicle type validation.
   - Existing vehicle license plate validation.
     
3. Check in vehicle to parking lot.
   - Nonregistered vehicle license plate validation.
   - Nonregistered parking lot ID validation.
   - Checking-in already parked vehicle validation.
   - Checking-in fully occupied parking lot validation.
     
4. Check out vehicle.
   - Nonregistered vehicle license plate validation.
   - Checking out vehicle that is not parked validation.
     
5. View parking lot details.
   - Nonregistered parking lot ID validation.
     
6. View all vehicles parked in a parking lot.
   - Empty parking lot validation
   - Nonregistered parking lot ID validation.




## Input / Output JSON data object Structure ##

Vehicle JSON structure sample: 
{
  "licensePlate":"IOP-1231",
  "type":"Car",
  "ownerName":"Juan Cruz"
}

Parking lot JSON structure sample: 
{
  "lotId":"LOT123",
  "location":"Manila",
  "Capacity":10,
  "occupiedSpaces":0
}


Input Validations:
1. licensePlate - Letters, numbers and dash (-) are the only allowed characters.
2. type - "Car", "Motorcycle" and "Truck" values only.
3. licensePlate - Letters and spaces ( ) are the only allowed characters.
4. lotId - cannot exceed to Maximum of 50 characters allowed.
5. occupiedSpaces - required input but overwritten zero (0) by default to avoid unbalanced occupancy and capacity.



## API Request format ##
Refer to the API collection in this path: "src/main/resources/Postman collection/SmartPark Collection.postman_collection.json"

1. Parking Lot registration.
   Postman URL: "localhost:8080/smartparking/register/parkinglot"
   Postman Method: POST
   Request Body: Parking lot JSON.
     
3. Vehicle registration
   Postman URL: "localhost:8080/smartparking/register/vehicle
   Postman Method: POST
   Request Body: Vehicle JSON.
     
5. Check in vehicle to parking lot.
   Postman URL: "localhost:8080/smartparking/checkin/vehicle"
   Postman Method: PUT
   Request Parameter: license-plate, parking-lot-id
     
7. Check out vehicle.
   Postman URL: "localhost:8080/smartparking/checkout/vehicle"
   Postman Metho : PUT
   Request Parameter: parking-lot-id
 
9. View parking lot details.
   Postman URL: localhost:8080/smartparking/view/parkinglot"
   Postman Method: GET
   Request Parameter: parking-lot-id
     
11. View all vehicles parked in a parking lot.
   Postman URL: "localhost:8080/smartparking/view/vehicles"
  Postman Metho: GET
Request Parameter: parking-lot-id
