# BSPlink commons REST

Provides common functionality to the REST services of the `BSPlink NFE project`.

- [Common exceptions](#common-exceptions)
- [Common error responses](#common-error-responses)
- [Exception handler](#exception-handler).


### Common exceptions

##### ApplicationException

Base class of all application exceptions, it will contain the error message and the associated HTTP status, all the rest of the application exceptions must extend this class in order to be properly captured by the exception handler.

##### ApplicationConflictException

Suitable for situations in which the user sends values with some kind of conflict with
values already saved in the application, for example when the user tries to add an e-mail
which already exists.

##### ApplicationValidationException

An specialized exception with the error result of a model validation. It can be used hassle free using the validation object `Errors`.

Example:

```java
    @PostMapping("/user")
    public ResponseEntity<String> add(@Valid @RequestBody User user, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApplicationValidationException(errors);
        }

         // … a bunch of shiny code lines

        return ResponseEntity.created(location).build();
    }
```

The HTTP status code of this exception is always **400 (bad request)**.


### Common error responses

The following responses are included:

##### ApplicationErrorResponse

Base class of all responses. Contains the minimal information about the error with the following fields:

- `timestamp`: UTC date and time when the error ocurred.
- `status`: HTTP status code.
- `error`: HTTP status descripción.
- `message`: custom error message.
- `path`: the path of the request that provoked the error.

Example:

```json
{
    "timestamp": 1525878009809,
    "status": 409,
    "error": "Conflict",
    "message": "Username already exists",
    "path": "/user"
}
```
##### ValidationErrorResponse

Aimed to return model validation errors, it has the same fields as `ApplicationErrorResponse` plus:

- `validationErrors`: an array containing an object for each validation error with the following fields:
    + `fieldName`: the field name in which the error occurred, it can be null if the error is not tied to a specific field.
    + `message`: the field validation error.

Example:

```json
{
    "timestamp": 1525878009809,
    "status": 400,
    "error": "Bad Request",
    "message": "Validation error",
    "path": "/user",
    "validationErrors": [
        {
            "fieldName": "username",
            "message": "Should be at least 2 characters length"
        },
        {
            "fieldName": "password",
            "message": "Cannot be empty"
        }
    ]
}
```


### Exception handler

An exception handler is added automatically when the package is included in a `Spring Boot` project.

The exception handler will capture all the exceptions of type `ApplicationException` and will create an `ApplicationErrorResponse` as the response body to be sent using the exception message and HTTP status to build a suitable response.

The handler extends [ResponseEntityExceptionHandler](https://docs.spring.io/spring-framework/docs/5.0.5.RELEASE/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html) so all the `Spring MVC` standard exceptions are also handled.

Any exception that is not `ApplicationException` or a `Spring MVC` standard exception will be converted to a generic internal server error without any sensitive information, i.e:

```json
{
    "timestamp": 1525878009809,
    "status": 500,
    "error": "Internal Server Error",
    "message": "Internal Server Error",
    "path": "/exception"
}
```

All original exceptions different than `ApplicationException` and all the created responses are always logged.


## How to use

Simply add the artifact repository URL and the dependency to your `build.gradle`:

```groovy
repositories {

    // ... other repositories

	maven {
	   url 'http://nexus.nfedev.accelya.com/repository/maven-public'
	}
}

dependencies {

    // ... other dependencies

    compile('org.iata.bsplink:bsplink-commons-rest:0.0.1')
}

```

note that the package expects that you have in your classpath the `Spring Web` dependencies (spring-web, spring-webmvc, …).

