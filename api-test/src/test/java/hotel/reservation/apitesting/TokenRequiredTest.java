package hotel.reservation.apitesting;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hotel.reservation.apitesting.testobjects.BookRoom;
import hotel.reservation.apitesting.testobjects.CancelReservation;
import hotel.reservation.apitesting.testobjects.Customer;
import hotel.reservation.apitesting.testobjects.CustomerCredentials;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenRequiredTest extends BaseClass {

    @Test
    public void firstTestSignup() throws JsonProcessingException, URISyntaxException {

        Customer testCustomer = new Customer(BaseClass.getUsername(),"123456","test@email.com","Test","User");
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
        String token = BaseClass.getExistingToken();
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
        String token = BaseClass.getToken();
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
    public void testBookRoomThenVerifyThenCancel() throws JsonProcessingException, URISyntaxException {
        String token = BaseClass.getToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        Integer roomNumber = 853;
        String d1 ="2020-10-10T17:24:56.081Z";
        String d2 ="2020-10-11T17:24:56.081Z";

        BookRoom availableRoomNoType = new BookRoom(roomNumber,d1,d2);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(availableRoomNoType);

        Response response = given()
        .contentType(ContentType.JSON)
        .body(json)
        .headers(headers)
        .accept(ContentType.JSON)
        .when().post(new URI("/bookroom"));

        response.then().assertThat().statusCode(HttpStatus.SC_OK)
        .body("customer.username",equalTo(BaseClass.getUsername()),"room.roomNumber",equalTo(roomNumber));

        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        CancelReservation cancelReservation = new CancelReservation(jsonPath.getInt("reservationNumber"));
        json = mapper.writeValueAsString(cancelReservation);

        given()
        .contentType(ContentType.JSON)
        .headers(headers)
        .accept(ContentType.JSON)
        .param("reservationNumber", jsonPath.getInt("reservationNumber"))
        .when().get(new URI("/viewreservation"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK)
        .body("customer.username",equalTo(BaseClass.getUsername()));


        token = BaseClass.getToken();
        headers = new HashMap<>();
        headers.put("Authorization",token);

        given()
        .contentType(ContentType.JSON)
        .body(json)
        .headers(headers)
        .accept(ContentType.JSON)
        .when().post(new URI("/cancelreservation"))
        .then().assertThat().statusCode(HttpStatus.SC_OK);

    }
}
