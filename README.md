
# Addison Global Backend Technical Assessment

*   Completed: 13/9/201

### Endpoints

A solution provided using Http4s, SBT (1.2.1) and Scala 2.12.

    /status
    
 Returns a Ok, 200, response if the service is running
 
    /auth
    
This endpoint defines a simple endpoint to offer the functionality of the **SimpleAsyncTokenService** implemented as request in the [Task Instructions](INSTRUCTIONS.md).

### Service Notes

*   Endpoints have CORS
*   Token is returned in Plain Text, an alternative solution would be to return the 'token' as a JWT Token.
*   Unit Tests are located in the [Test directory](/src/test/scala/com/)
*   An integration test is located in the [IT directory](/src/it/scala/com)

#### Running Test

Running the service

    sbt run
    
##### Running the tests

    sbt test
    
##### Running the Integrations Tests

Start an instance

    sbt run
    
Run the tests against the running service    

    sbt it:test 

#### Load Testing Locally

Concurrency = 100
Number of Requests = 500

    ab -T 'application/json'  -n 500 -c 100 -p POST.data http://localhost:8080/auth
