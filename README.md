
# hotel-booking-api
Project to implement a booking REST API for a hotel.

**Author:** Henrique Romano

## Technologies
- Java (JDK 11)
- Spring Boot
   - Spring Boot DevTools
   - Spring Web
   - Spring Data JPA
- Maven
- H2 database
- JUnit 5 and Mockito for tests
- Swagger

## Description
The goal to this API is to expose endpoints that can be used by a frontend application to:

 1. Check availability
 2. Book a reservation
 3. Modify a reservation
 4. Cancel a reservation

### Business requirements:
- There is only one room in the hotel
- Stays cannot be longer than 3 days
- Stays cannot be reserved more than 30 days in advance
- Reservations start at least the next day of booking
- A 'DAY' in the hotel room starts from 00:00:00 to 23:59:59
- All customers can create, modify or cancel any booking for the room

## API endpoints

 - GET `/booking/all`
	 - Returns all bookings.
 - POST `/booking/new`
	 - Create new booking.
	 - name, startDate and endDate are required
 - POST `/booking/cancel/{id}`
	 - Cancel booking by it's id
 - POST `/booking/update/{id}`
	 - Update booking by it's id
	 - All fields are optional, so you can update some or all of the following values: name, startDate, endDate
 - POST `/booking/check`
	 - Check bookings within two dates. 
	 - By passing startDate and endDate, the api returns all bookings within the dates.


## Instructions

### How to build and run

Maven is required in order to build. You can use Maven or Java to run.

From your terminal, go to the root folder of this project and run the following command:

    mvn clean install && mvn spring-boot:run
If there are no errors in the output, the API should be running, and available under *localhost:8080/booking*

### Making requests
You can try the */booking* api by:
1) using sample Postman collection present in root folder: ***hotel-booking-api.postman_collection.json***, or by
2) using swagger-ui: http://localhost:8080/swagger-ui.html#/booking-controller
