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

```java
// UserController
// When we name a header specifically, the header is required by default
// Requesting headers: 

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("")
    public void createUser(
            @RequestHeader("email") String email, // this will pull headers individually by key
            @RequestHeader("password") String password) {
        System.out.println("email is: " + email);
        System.out.println("password is: " + password);
    }
}

// you can all request multiple headers all together in a map
@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("")
    public void createUser( // method signiture (access, return, name and parameters)
            @RequestHeader Map<String, String> headers) {
        System.out.println("email is: " + email);
        System.out.println("password is: " + password);
    }
}
```

```java

// UserController
// Handling Exceptions by returning  a ResponseEntity
@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(
        @RequestHeader("email") String email,
        @RequestHeader("password") String password
    ) {
        try {
            UserResponse userResponse = userService.createUser(email, password);
            return ResponseEntity
                    .status(HttpStatus.CREATED) // 201
                    .body(userResponse);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409
                    .header("message", "Email already in use")
                    .build();
        }

    }
}

```

```java
// UserService

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(String email, String password) {
        User newUser = new User(email, password);
        User savedUser = userRepository.save(newUser);
        return UserResponse.builder() // builds and returns UserResponse object
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .build();
    }
}

```

```java
// User

@AllArgsConstructor
@Entity
@Table(name = "`user`", uniqueConstraints = {@UniqueConstraint(columnNames = "email")}) // sets unique contsraint for email column that it has to be unique
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this. password = password;
    }
}
```


## Exceptions



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
}
```

## Hibernate

- ORM (object relational mapping framework)
- ORM is basicaly making your models match db tables so that data aligns
- used to overcome shortcomings of JDBC (java database connection)


```java
// User
@Entity
@Table(name = "`user`") //'user' is  a reserved word in postgres and so it needs to be written with ``
@Data
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}

```

## JpaRepository

```java
// UserRepository

import com.dap.DailyArtPrompt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // defining a custom method for the interface. So long as the name is intuitive this will function properly
}

```

## Lombok

- add dependency in build.gradle `id "io.freefair.lombok" version "5.0.0-rc2"`

```java
// User
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

 @Entity
 @Data // annotation creates getters and setters
@Table(name = "`user`", uniqueConstraints = {@UniqueConstraint(columnNames = "email")}) // sets constraint that email column is unique
@AllArgsConstructor
@NoArgsConstructor
 public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private long id;
 
     @Column(name = "email")
     private String email;
 
     @Column(name = "password")
     private String password;
 }
```

```java
@Component
@RequiredArgsConstructor // this allows you to declare that UserService class has a dependency of userRepository without writing it out (see long version below)
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(String email, String password) {
      
    }
}

@Component
//@RequiredArgsConstructor // needs constructor without this annotation
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(String email, String password) {
    }
}
```

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