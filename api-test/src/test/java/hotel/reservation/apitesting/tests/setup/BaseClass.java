package hotel.reservation.apitesting.tests.setup;

import static io.restassured.RestAssured.*;

import org.junit.BeforeClass;


public class BaseClass {
   
    @BeforeClass
    public static void setUp() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "";
    }

   
}
