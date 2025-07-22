# Backend - Employee Collaboration Service

This module represents the backend of the project. It processes employee and project information to determine which pair of employees has worked together for the longest total time across shared projects.

## Technologies Used
- Java 21
- Spring Boot 3.2.5
- Maven
- Lombok
- Spring Boot

---

## Prerequisites

- JDK 21
- Maven 3.8+

---

## How to Run

1. Open a terminal and navigate to the `backend/` directory.

2. Build the project:

```bash
mvn clean install
```

3. Build the project:

```bash
mvn spring-boot:run
```
4. The main endpoint will be available at:

```bash
POST http://localhost:8080/api/employee/calculate-time-together
```

It accepts a list of employees and their projects, and returns the pair with the highest total days worked together, including the shared project IDs.

---

## Logging Configuration (`logback-spring.xml`)

The backend uses Logback for logging. The `logback-spring.xml` file is placed in `src/main/resources` to configure log output for both the console and log files.

Example configuration:

```xml
<configuration>

    <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"/>

    <property name="ERROR_LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n%xThrowable{5}"/>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/app-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/error-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>${ERROR_LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
        <appender-ref ref="ERROR_FILE" />
    </root>

    <logger name="org.springframework" level="WARN" />
    <logger name="org.hibernate" level="WARN" />
    <logger name="org.apache.http" level="WARN" />
    <logger name="org.apache.catalina" level="WARN" />
    <logger name="org.flywaydb" level="WARN" />

    <logger name="com.alberto.sindratest" level="DEBUG" />

</configuration>
```

## Logging Usage

The backend uses Lombok’s `@Slf4j` annotation to enable logging via SLF4J. This provides a `log` variable in any class where the annotation is used, without needing to manually declare or initialize a logger.

### Example: Logging in a Service

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CompanyServiceImp {
  private static final Logger log = LoggerFactory.getLogger(CompanyServiceImp.class);

    public void executeBusinessLogic() {
        log.info("Business logic execution started.");

        try {
            // some business logic
            log.debug("Processing employee pairs...");
        } catch (Exception ex) {
            log.error("Unexpected error occurred during processing", ex);
        }

        log.info("Business logic execution completed.");
    }
}
```
- Use log.info() for general application flow messages.
- Use log.debug() for verbose output during development or debugging.
- Use log.error() to capture exceptions and failures.
  
The logs are written both to the console and to a rotating file located in the /logs folder (as defined in logback-spring.xml)

---
## Validation and Error Handling

The backend implements input validation and centralized exception handling to ensure data integrity and user-friendly error responses.

### Input Validation

The API validates incoming requests using Jakarta Bean Validation annotations such as `@Valid`, `@NotNull`, and custom validation constraints.

Example of a validated controller method:

```java
@PostMapping("employee/calculate-time-together")
public ResponseEntity<List<PairResult>> calculateTimePairEmployees(@RequestBody @Valid List<CompanyInformation> companyInformation) {
    if (companyInformation == null || companyInformation.isEmpty()) {
        throw new CompanyException("Request body is required and cannot be empty");
    }
    return ResponseEntity.ok(companyService.findLongestPartnersByProject(companyInformation));
}
```

The `CompanyInformation` DTO is validated to ensure required fields are present and correctly formatted.

### Custom Business Exception

Business logic errors are handled using a custom runtime exception:

```java
public class CompanyException extends RuntimeException {
    public CompanyException(String message) {
        super(message);
    }
}
```

### Global Exception Handler

A centralized `@RestControllerAdvice` class captures exceptions and returns consistent error messages:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<Map<String, String>> handleBusinessError(CompanyException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", "Malformed JSON or missing request body."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralError(Exception ex) {
        return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
    }
}
```

This ensures:
- `400 Bad Request` is returned for invalid or missing input.
- `500 Internal Server` Error is returned for unexpected failures.
- All responses contain a consistent JSON structure:
`{"message": "Error description"}`