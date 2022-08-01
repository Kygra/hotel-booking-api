
# hotel-booking-api
Booking API for Hotel Cancun.

**Author:** Henrique Romano

## Description

The requirements are:

- There is only one room in the hotel
- Stays cannot be longer than 3 days
- Stays cannot be reserved more than 30 days in advance
- Reservations start at least the next day of booking
- To simplify the use case, a 'DAY' in the hotel room starts from 00:00 to 23:59:59.
- Everyone can:
  - check the room availability
  - place a reservation
  - cancel it
  - modify it
- API can be insecure
- Quality of service must be 99.99 to 100% => no downtime

To simplify:
- The API is insecure
- The API is not thread safe
- Integration tests were not implemented
- The database used is temporary - H2 in-memory db
- There is no guarantee of 99.99% availability - it could be possible by hosting this in Google Cloud and setting up the necessary configurations
- All customers can create, modify or cancel any booking for the room
- All values of Booking object can be passed in the body request of:
  - GET /booking (startDate and endDate are required) 
  - POST /booking/new (name, startDate and endDate are required)
  - POST /booking/update/{id} (id is required; name, startDate and endDate are optionals)

## Technologies

To meet previous requirements, I have decided to implemented a REST API, and I have used the following technologies:
- Java (JDK 11)
- Spring Boot
   - Spring Boot DevTools
   - Spring Web
   - Spring Data JPA
- Maven
- H2 database (in-memory db)
- JUnit 5 and Mockito for tests
- Swagger


## Steps to run

Maven is required in order to build. You can use maven or java to run.

From root folder of this project, run:

    mvn clean install && mvn spring-boot:run

You can make requests to the /booking api by:
1) using sample Postman collection in root folder: ***hotel-booking-api.postman_collection.json***
2) using swagger: http://localhost:8080/swagger-ui.html#/booking-controller