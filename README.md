# Hostfully Technical Test - Joao Goulart

This repository contains the implementation for the Hostfully technical test.

## Architecture

The project follows a typical Spring Boot architecture with the following main components:

- **Controllers**: Responsible for handling incoming HTTP requests.
    - [BlockController](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/controllers/BlockController.java)
    - [BookingController](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/controllers/BookingController.java)

- **Entities**: Represents the main entities or models used in the application.
    - [Block](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/entities/Block.java)
    - [Booking](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/entities/Booking.java)

- **Repositories**: Interfaces for database operations.
    - [BlockRepository](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/repositories/BlockRepository.java)
    - [BookingRepository](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/repositories/BookingRepository.java)

- **Services**: Contains the business logic.
    - [BlockService](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/services/BlockService.java)
    - [BookingService](https://github.com/goulartt/Hostfully-Case/blob/master/src/main/java/com/hostfully/technicaltest/services/BookingService.java)

## Java Version

The project is developed using Java 17. 

## IDE

The project was developed using the IntelliJ IDEA.

## Testing

### Unit Tests

Unit tests focus on testing individual components in isolation. Here are some of the unit tests available in the repository:

- [BlockServiceTest](https://github.com/goulartt/Hostfully-Case/blob/master/src/test/java/com/hostfully/technicaltest/services/BlockServiceTest.java)
- [BookingServiceTest](https://github.com/goulartt/Hostfully-Case/blob/master/src/test/java/com/hostfully/technicaltest/services/BookingServiceTest.java)

### Integration Tests

Integration tests focus on testing the interaction between components. Here are some of the integration tests available in the repository:

- [BlockIntegrationTest](https://github.com/goulartt/Hostfully-Case/blob/master/src/test/java/com/hostfully/technicaltest/integration/BlockIntegrationTest.java)
- [BookingIntegrationTest](https://github.com/goulartt/Hostfully-Case/blob/master/src/test/java/com/hostfully/technicaltest/integration/BookingIntegrationTest.java)
