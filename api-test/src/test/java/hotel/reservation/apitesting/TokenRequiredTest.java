package hotel.reservation.apitesting;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.ishapirov.hotelapi.reservationservice.domain.CancelReservation;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationResponse;
import com.ishapirov.hotelapi.roomservice.domain.RoomResponse;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomTypeInformation;
import com.ishapirov.hotelapi.reservationservice.domain.BookRoom;
import com.ishapirov.hotelapi.authenticationservice.credentials.UserCredentials;
import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenRequiredTest extends BaseClass{

    private MyObjectMapper mapper = new MyObjectMapper();

    private UsernameTokenCreater usernameTokenCreater = UsernameTokenCreater.getInstance();

    @Test
    public void firstTestSignup() throws JsonProcessingException, URISyntaxException {

        UserSignupInformation testCustomer = new UserSignupInformation(usernameTokenCreater.getUsername(),"Coolpass-123","test@email.com","Test","User");
        String json = mapper.writeValueAsString(testCustomer);

        UserInformation userInformation = new UserInformation();
        userInformation.setUsername(usernameTokenCreater.getUsername());
        userInformation.setEmail("test@email.com");
        userInformation.setFirstName("Test");
        userInformation.setLastName("User");


        given()
        .contentType(ContentType.JSON)
        .body(json)
        .accept(ContentType.JSON)
        .when().post(new URI("/services/users"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK)
        .body("username",equalTo(userInformation.getUsername()),
                "email",equalTo(userInformation.getEmail()),
                "firstName",equalTo(userInformation.getFirstName()),
                "lastName",equalTo(userInformation.getLastName()));
    }

    @Test
    public void badPasswordSignupTest() throws JsonProcessingException, URISyntaxException {

        UserSignupInformation testCustomer = new UserSignupInformation("RandomName","123456","test@email.com","Test","User");
        String json = mapper.writeValueAsString(testCustomer);


        given()
                .contentType(ContentType.JSON)
                .body(json)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void authenticateTest() throws  JsonProcessingException, URISyntaxException {

        UserCredentials testCustomer = new UserCredentials("cooluser","Coolpass-123");
        String json = mapper.writeValueAsString(testCustomer);
        
        given()
        .contentType(ContentType.JSON)
        .body(json)
        .accept(ContentType.JSON)
        .when()
        .post(new URI("/services/authentication")).then().assertThat()
        .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void testViewReservation() throws URISyntaxException, JsonProcessingException, ParseException {
        String token = usernameTokenCreater.getExistingToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);
        StdDateFormat format = new StdDateFormat();
        Date checkIn = format.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = format.parse("2110-10-11T17:24:56.081Z");
        ReservationInformation reservationInformation = new ReservationInformation(2086, "cooluser",1986,checkIn,checkOut);
        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        JsonPath jsonPath = JsonPath.from(reservationInformationJson);

        given()
        .contentType(ContentType.JSON)
        .headers(headers)
        .accept(ContentType.JSON)
        .when().get(new URI("/services/reservations/2086"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK)
        .body("",equalTo(jsonPath.get()));
    }

    @Test
    public void testViewReservationResponse() throws URISyntaxException, JsonProcessingException, ParseException {
        String token = usernameTokenCreater.getExistingToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);
        StdDateFormat format = new StdDateFormat();
        Date checkIn = format.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = format.parse("2110-10-11T17:24:56.081Z");
        RoomTypeInformation roomTypeInformation = new RoomTypeInformation("Single");
        RoomResponse roomResponse = new RoomResponse(1986,roomTypeInformation,150.);
        UserInformation userInformation = new UserInformation(2085,"cooluser","cooluser@gmail.com","Joe","Bob");
        ReservationResponse reservationInformation = new ReservationResponse(2086, userInformation,roomResponse,checkIn,checkOut);
        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        JsonPath jsonPath = JsonPath.from(reservationInformationJson);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/reservations/response/2086"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));
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
        .param("reservationNumber", "2086")
        .when().get(new URI("/services/reservations/2086"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testBookRoomThenVerifyThenCancel() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreater.getToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        Integer roomNumber = 2035;
        StdDateFormat format = new StdDateFormat();
        Date d1 = format.parse("2110-10-10T17:24:56.081Z");
        Date d2 = format.parse("2110-10-15T17:24:56.081Z");

        BookRoom bookRoom = new BookRoom(roomNumber,d1,d2);
        String json = mapper.writeValueAsString(bookRoom);

        Response response = given()
        .contentType(ContentType.JSON)
        .body(json)
        .headers(headers)
        .accept(ContentType.JSON)
        .when().post(new URI("/services/reservations"));

        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        int reservationNumber = jsonPath.getInt("reservationNumber");

        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber, usernameTokenCreater.getUsername(),roomNumber,d1,d2);
        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        jsonPath = JsonPath.from(reservationInformationJson);

        response.then().assertThat().statusCode(HttpStatus.SC_OK)
        .body("",equalTo(jsonPath.get()));

        //Viewing reservation
        given().contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/reservations/"+reservationNumber))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));

        //Viewing all reservations for user (1)
        given().contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .param("username",usernameTokenCreater.getUsername())
                .when().get(new URI("/services/reservations/"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("content.get(0)", equalTo(jsonPath.get()),
                        "last", equalTo(true),
                        "totalElements",equalTo(1));


        //Testing availability of room at different time intervals
        //CheckIn: Prior to start. Checkout: Inside interval. Should be unavailable
        String checkInTest = "2110-10-09T17:24:56.081Z";
        String checkOutTest = "2110-10-13T17:24:56.081Z";

        given().contentType(ContentType.JSON)
            .param("checkInDate",checkInTest)
            .and().param("checkOutDate",checkOutTest)
            .accept(ContentType.JSON)
            .when().get(new URI("/services/rooms/" + roomNumber)).then().assertThat()
            .statusCode(HttpStatus.SC_CONFLICT);

        //CheckIn: Inside interval. Checkout: Outside interval. Should be unavailable
        checkInTest = "2110-10-12T17:24:56.081Z";
        checkOutTest = "2110-10-16T17:24:56.081Z";


        given().contentType(ContentType.JSON)
                .param("checkInDate",checkInTest)
                .and().param("checkOutDate",checkOutTest)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomNumber)).then().assertThat()
                .statusCode(HttpStatus.SC_CONFLICT);

        //Testing availability of room at different time intervals
        //Checkin: Outside interval. Checkout: Outside interval. Should be unavailable
        checkInTest = "2110-10-09T17:24:56.081Z";
        checkOutTest =  "2110-10-16T17:24:56.081Z";


        given().contentType(ContentType.JSON)
                .param("checkInDate",checkInTest)
                .and().param("checkOutDate",checkOutTest)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomNumber)).then().assertThat()
                .statusCode(HttpStatus.SC_CONFLICT);


        CancelReservation cancelReservation = new CancelReservation(reservationNumber);
        json = mapper.writeValueAsString(cancelReservation);
        
        given()
            .contentType(ContentType.JSON)
            .body(json)
            .headers(headers)
            .accept(ContentType.JSON)
            .when().delete(new URI("/services/reservations/"+reservationNumber))
            .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);

    }
}
