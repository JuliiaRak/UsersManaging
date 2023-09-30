# UsersManaging
RESTful API for managing users using Spring Boot 

## Task overview:
This task involved creating a RESTful API based on Spring Boot to manage users and their data. The requirements included the implementation of the "Users" resource with certain fields and functionality. The resource should follow RESTful API design best practices, be covered by unit tests, have error handling, and responses in JSON format.

## Implementation of the task:
* Development of the user model (User): First, a user model was created with mandatory fields (Email, First Name, Last Name, Birth Date) and optional fields (Address, Phone Number). Email template validation has been added to the Email field.
* Development of a service class (UserService): A service class was created to perform user operations such as create, update, delete and search. The service class works with a list of users and does not require the use of a database to store information.
* Development of the controller (UserController): The controller was implemented to process HTTP requests and interact with the service class. It contains methods for creating, updating, deleting, and searching for users by date of birth range. The controller also validates data and returns responses in JSON format.
* Error handling and JSON responses: The controller has implemented error handling for RESTful APIs by using the @ControllerAdvice annotation. Responses in controller methods are always formatted in JSON format.
* Unit Tests: Unit tests were written for the service class (UserService) and the controller (UserController). The tests test different user experience scenarios, including create, update, delete, and search. Tests were also written to check data validation and error handling.
