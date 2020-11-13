package hotel.reservation.apitesting.tests.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import com.ishapirov.hotelapi.services.authentication.credentials.UserCredentials;
import hotel.reservation.apitesting.mapper.MyObjectMapper;
import hotel.reservation.apitesting.testdata.UsernameTokenCreator;
import hotel.reservation.apitesting.tests.setup.BaseClass;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;


public class AuthenticationTests  extends BaseClass {
    private MyObjectMapper mapper = new MyObjectMapper();

    @Test
    public void correctPasswordTest() throws JsonProcessingException, URISyntaxException {

        UserCredentials testCustomer = new UserCredentials("cooluser","Coolpass-123");
        String json = mapper.writeValueAsString(testCustomer);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .accept(ContentType.JSON)
                .when()
                .post(new URI("/services/authentication")).then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("",hasKey("token"));

    }

    @Test
    public void incorrectPasswordTest() throws JsonProcessingException, URISyntaxException {

        UserCredentials testCustomer = new UserCredentials("cooluser","Coolpass-1234");
        String json = mapper.writeValueAsString(testCustomer);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .accept(ContentType.JSON)
                .when()
                .post(new URI("/services/authentication")).then().assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    public void weakPasswordTest()throws JsonProcessingException, URISyntaxException {

        UserCredentials testCustomer = new UserCredentials("cooluser","Coolpass");
        String json = mapper.writeValueAsString(testCustomer);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .accept(ContentType.JSON)
                .when()
                .post(new URI("/services/authentication")).then().assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }
}
