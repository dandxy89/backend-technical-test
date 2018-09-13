
# Addison Global Backend Technical Assessment

### Endpoints

A solution provided using Http4s, SBT (1.2.1) and Scala 2.12.

    /status
    
 Returns a Ok, 200, response if the service is running
 
    /auth
    
This endpoint defines a simple endpoint to offer the functionality of the **SimpleAsyncTokenService** implemented as request in the [Task Instructions](INSTRUCTIONS.md).

#### Notes

*   Endpoints have CORS
*   Token is returned in Plain Text, an alternative solution would be to return the 'token' as a JWT Token.
*   Unit Tests are located in the [Test directory](/src/test/scala/com/)
*   An integration test is located in the [IT directory](/src/it/scala/com)
