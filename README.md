# Daily Art Prompt Service (Spring Boot)

# Java

- check version `java -version`

## Hello World

```java
public class HelloWorld {
  public static void main(String[] args) {
    System.out.println("Hello World!");
  }
}
```

## IntelliJ
   
- To run java code you first need to compile it. To compile code in the terminal use command: `javac fileName.java`
- If the file is successfully compiled it will create a fileName.class file that can be run
- To run this file use the command: `java fileName`

- You can also run the app with IntelliJ configurations. To build the gradle project make sure you right click on build.gradle and click 'import Gradle Project' after this you can then click the play button on the upper right to run the project

## Comments

- When comments are short we use the single-line syntax: `//`
- When comments are long we use the multi-line syntax: `/* */`

## Tips

- The controller should be clean of functionality and only contain the endpoints
- Keep private methods small as they can not be tested directly
- Keep methods LOOSELY coupled so they are not dependent on each other and are easier to test and scale

## Spring Boot

- Spring is a Java Framework that makes creating Java applications faster and easier:

  - Sets up the boilerplate for an application that gets it up and running quickly
  - Automates configuration
  - Spring initializer is used to generate and application with customizable dependencies of your choice

- use Spring Initializr to Bootstrap your application at https://start.spring.io/

- Maven VS Gradle:

  - Maven is a rigid model while Gradle is more flexible and easily customizable
  - Google is using Gradle as the build system for Android Studio
  - For more details about these two build automations visit https://dzone.com/articles/gradle-vs-maven

- .war vs .jar:
  - A .war file is a Web Application Archive which runs inside an application server while a .jar is Java Application Archive that runs a desktop application on a user's machine. A war file is a special jar file that is used to package a web application to make it easy to deploy it on an application server.

### Gradle

- To run a build initially use command `gradle build` to add dependencies
- Use command `gradle bootRun` to execute file
- Add the dependency of `implementation 'org.springframework.boot:spring-boot-starter-web'` to have web development dependency this will make your application run on localhost: 8080 when application is executed

- when opening gradle project in IntelliJ two finger click the build.gradle file and click `import gradle project` to import as a gradle project

- Run gradle application using profiles

```sh

gradle bootRun --args="--spring.profiles.active=local" # local is the profile name in this example which is application-local.yml

```

## Restful Web Service

- REST stands for Representational State Transfer. It's an architectural pattern for creating web services. A RESTful service is one that implements that pattern and follows the naming conventions of big to small

- Example: dog/{id}/owner/{id}  (with this we can see that owner is a part of dog)

```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController // Use RestController and RequestMapping to add listeners to the route
@RequestMapping("/dog")
public class DogController {
    DogService dogService = new DogService(); // dogService must be instantiated so that we can use its methods

    @GetMapping("/greeting")
    public String dogGreeting() {
        return dogService.getDogGreeting();
    }

    @GetMapping("/new")
    public Dog new( // Use RequestParam to be able to pass parameters to api call
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

```bash
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

```bash
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.postgresql:postgresql'
```

- Make sure to properly link service to controller and pass service to controller in constructor

```java

// Controller

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