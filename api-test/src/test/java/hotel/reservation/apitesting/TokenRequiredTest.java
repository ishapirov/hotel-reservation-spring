package hotel.reservation.apitesting;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.ishapirov.hotelapi.*;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenRequiredTest extends BaseClass{

    private UsernameTokenCreater usernameTokenCreater = UsernameTokenCreater.getInstance();

    @Test
    public void firstTestSignup() throws JsonProcessingException, URISyntaxException {

        CustomerSignupInformation testCustomer = new CustomerSignupInformation(usernameTokenCreater.getUsername(),"123456","test@email.com","Test","User");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testCustomer);

        given()
        .contentType(ContentType.JSON)
        .body(json)
        .accept(ContentType.JSON)
        .when().post(new URI("/signup"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK);
    }
    
    @Test
    public void authenticateTest() throws  JsonProcessingException, URISyntaxException {

        CustomerCredentials testCustomer = new CustomerCredentials("cooluser","123456");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testCustomer);
        
        given()
        .contentType(ContentType.JSON)
        .body(json)
        .accept(ContentType.JSON)
        .when()
        .post(new URI("/authenticate")).then().assertThat()
        .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void testViewReservation() throws URISyntaxException, JsonProcessingException {
        String token = usernameTokenCreater.getExistingToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        given()
        .contentType(ContentType.JSON)
        .headers(headers)
        .accept(ContentType.JSON)
        .param("reservationNumber", "880")
        .when().get(new URI("/viewreservation"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK)
        .body("reservationNumber",equalTo(880),"customer.username",equalTo("cooluser"));
    }

    @Test
    public void testViewForbiddenReservation() throws URISyntaxException, JsonProcessingException {
        String token = usernameTokenCreater.getToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        given()
        .contentType(ContentType.JSON)
        .headers(headers)
        .accept(ContentType.JSON)
        .param("reservationNumber", "880")
        .when().get(new URI("/viewreservation"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testBookRoomThenVerifyThenCancel() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreater.getToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        Integer roomNumber = 853;
        Date d1 = DateFormat.getDateInstance().parse( "2010-10-10T17:24:56.081Z");
        Date d2 = DateFormat.getDateInstance().parse( "2010-10-10T17:24:56.081Z");

        BookRoom bookRoom = new BookRoom(roomNumber,d1,d2);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bookRoom);
        Response response = given()
        .contentType(ContentType.JSON)
        .body(json)
        .headers(headers)
        .accept(ContentType.JSON)
        .when().post(new URI("/bookroom"));
 
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
        .body("customer.username",equalTo(usernameTokenCreater.getUsername()),"room.roomNumber",equalTo(roomNumber));
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        int reservationNumber = jsonPath.getInt("reservationNumber");
       
        given()
        .contentType(ContentType.JSON)
        .headers(headers)
        .accept(ContentType.JSON)
        .param("reservationNumber", reservationNumber)
        .when().get(new URI("/viewreservation"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK)
        .body("customer.username",equalTo(usernameTokenCreater.getUsername()));
        //Testing availability of room at different time intervals
        //Checkin: Prior to start. Checkout: Inside interval. Should be unavailable
        d1 = DateFormat.getDateInstance().parse( "2010-10-9T17:24:56.081Z");
        d2 = DateFormat.getDateInstance().parse( "2010-10-13T17:24:56.081Z");

        given().contentType(ContentType.JSON)
        .param("checkInDate",d1)
        .and().param("checkOutDate",d2)
        .and().param("roomNumber",roomNumber)
        .accept(ContentType.JSON)
        .when().get(new URI("/getroomifavailable")).then().assertThat()
        .statusCode(HttpStatus.SC_CONFLICT);

        //Checkin: Inside interval. Checkout: Outside interval. Should be unavailable
        d1 = DateFormat.getDateInstance().parse( "2010-10-12T17:24:56.081Z");
        d2 = DateFormat.getDateInstance().parse( "2010-10-16T17:24:56.081Z");


        given().contentType(ContentType.JSON)
        .param("checkInDate",d1)
        .and().param("checkOutDate",d2)
        .and().param("roomNumber",roomNumber)
        .accept(ContentType.JSON)
        .when().get(new URI("/getroomifavailable")).then().assertThat()
        .statusCode(HttpStatus.SC_CONFLICT);

        //Testing availability of room at different time intervals
        //Checkin: Outside interval. Checkout: Outside interval. Should be unavailable
        d1 = DateFormat.getDateInstance().parse( "2010-10-9T17:24:56.081Z");
        d2 = DateFormat.getDateInstance().parse( "2010-10-16T17:24:56.081Z");


        given().contentType(ContentType.JSON)
        .param("checkInDate",d1)
        .and().param("checkOutDate",d2)
        .and().param("roomNumber",roomNumber)
        .accept(ContentType.JSON)
        .when().get(new URI("/getroomifavailable")).then().assertThat()
        .statusCode(HttpStatus.SC_CONFLICT);


        CancelReservation cancelReservation = new CancelReservation(reservationNumber);
        json = mapper.writeValueAsString(cancelReservation);
        
        given()
        .contentType(ContentType.JSON)
        .body(json)
        .headers(headers)
        .accept(ContentType.JSON)
        .when().post(new URI("/cancelreservation"))
        .then().assertThat().statusCode(HttpStatus.SC_OK);

    }
}
