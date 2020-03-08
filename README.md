# Daily Art Prompt Service (Spring Boot)

- Spring is a Java Framework that makes creating Java applications faster and easier:

  - Sets up the boilerplate for an application that gets it up and running quickly
  - Automates configuration
  - Spring initializer is used to generate and application with customizable dependencies of your choice

- use Spring Initializr to Bootstrap your application at https://start.spring.io/

## Restful Web Service

- REST stands for Representational State Transfer. It's an architectural pattern for creating web services. A RESTful service is one that implements that pattern and follows the naming conventions of big to small

- Example: owner/{id}/dog/{id}  (with this we can see that owner has dogs and can also have multiple)

```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController // Use RestController and RequestMapping to add listeners to the route
@RequestMapping("/dogs")
public class DogController {
    DogService dogService = new DogService(); // dogService must be instantiated so that we can use its methods

    @GetMapping("")
    public String getDog() {
        return dogService.getDog();
    }

    @PostMapping("/new")
    public Dog new(
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam int age,
        @RequestParam boolean goodDog,
        @RequestParam Dog.Breed breed
        ) {
        return dogService.createNewDog(firstName, lastName, age, goodDog, breed);
    }
}
```

## DB Connection with Spring

- You should have a application.properties file delete this and create a application.yml file instead and and the below code to it

```shell script
### Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dogsdb # add your db name here in the end. in our case, dogsdb / port number default to 5432
    username: sasha #default is set to your computer username can find this by typing command `whoami` in your terminal
    password: # if you have one
    driverClassName: org.postgresql.Driver
  jpa:
    properties.hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect # The SQL dialect makes Hibernate generate better SQL for the chosen database
    hibernate.ddl-auto: update # Hibernate ddl auto (create, create-drop, validate, update)
```

- Add these dependencies/drivers to the build.gradle file (if this a gradle project)

```shell script
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.postgresql:postgresql'
```

- Make sure to properly link service to controller and pass service to controller in constructor

```java

@RestController
@RequestMapping("/prompt")
public class PromptController {

    private final PromptService promptService;

    @Autowired
    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

// Service
@Component
public class PromptService {

    final PromptRepository promptRepository;

    public PromptService(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }
```

## Hibernate

- ORM (object relational mapping framework)
- ORM is basicaly making your models match db tables so that data aligns
- used to overcome shortcomings of JDBC (java database connection)

# Testing

- a file like `fileName.java` will have a test file with a name of `fileNameTest.java`
- `Cmd + Shift + t` This finds the file/test file associated with the one you are currently in
- change IntelliJ settings to allow Gradle to show test names: 
   - Preferences > Build, Execution, Deployment > Build Tools > Gradle > Run tests using `IntelliJ IDEA`
   - Make sure you select the correct application that you want this to apply to
 
## JUnit

 - `gradle test` Runs test through terminal
 
 - Can also run with intelliJ, but make sure
 
 - Add to build.gradle in order to add logging for tests:

```groovy
test {
	useJUnitPlatform()
	testLogging {
		events = ["passed", "skipped", "failed"]
		exceptionFormat = "full"
	}
}
```

- AssertJ library import is: 

`import static org.assertj.core.api.Assertions.assertThat;`

- Change test name by using annotation `@DisplayName()`

## Tips

- The controller should be clean of functionality and only contain the endpoints
- Keep private methods small as they can not be tested directly
- Keep methods LOOSELY coupled so they are not dependent on each other and are easier to test and scale