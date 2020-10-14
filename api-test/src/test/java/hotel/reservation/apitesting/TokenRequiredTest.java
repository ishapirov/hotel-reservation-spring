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
import com.ishapirov.hotelapi.cancel.CancelReservation;
import com.ishapirov.hotelapi.domainapi.CustomerInformation;
import com.ishapirov.hotelapi.domainapi.ReservationInformation;
import com.ishapirov.hotelapi.domainapi.RoomInformation;
import com.ishapirov.hotelapi.domainapi.RoomTypeInformation;
import com.ishapirov.hotelapi.formdata.BookRoom;
import com.ishapirov.hotelapi.formdata.CustomerCredentials;
import com.ishapirov.hotelapi.formdata.CustomerSignupInformation;
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

        CustomerSignupInformation testCustomer = new CustomerSignupInformation(usernameTokenCreater.getUsername(),"123456","test@email.com","Test","User");
        String json = mapper.writeValueAsString(testCustomer);

        CustomerInformation customerInformation = new CustomerInformation(usernameTokenCreater.getUsername(),"test@email.com","Test","User");
        String customerInformationJson = mapper.writeValueAsString(customerInformation);
        JsonPath jsonPath = JsonPath.from(customerInformationJson);

        given()
        .contentType(ContentType.JSON)
        .body(json)
        .accept(ContentType.JSON)
        .when().post(new URI("/signup"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_OK)
        .body("",equalTo(jsonPath.get()));
    }
    @Test
    public void authenticateTest() throws  JsonProcessingException, URISyntaxException {

        CustomerCredentials testCustomer = new CustomerCredentials("cooluser","123456");
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
    public void testViewReservation() throws URISyntaxException, JsonProcessingException, ParseException {
        String token = usernameTokenCreater.getExistingToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);
        StdDateFormat format = new StdDateFormat();
        Date checkIn = format.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = format.parse("2110-10-11T17:24:56.081Z");
        RoomTypeInformation roomTypeInformation = new RoomTypeInformation("Single");
        RoomInformation roomInformation = new RoomInformation(912,roomTypeInformation,204.);
        CustomerInformation customerInformation = new CustomerInformation("cooluser","cooluser@gmail.com","Joe","Bob");
        ReservationInformation reservationInformation = new ReservationInformation(1015,customerInformation,roomInformation,checkIn,checkOut);
        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        JsonPath jsonPath = JsonPath.from(reservationInformationJson);

        given()
        .contentType(ContentType.JSON)
        .headers(headers)
        .accept(ContentType.JSON)
        .param("reservationNumber", "1015")
        .when().get(new URI("/viewreservation"))
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
        .param("reservationNumber", "1015")
        .when().get(new URI("/viewreservation"))
        .then()
        .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testBookRoomThenVerifyThenCancel() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreater.getToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        Integer roomNumber = 970;
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
        .when().post(new URI("/bookroom"));

        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        int reservationNumber = jsonPath.getInt("reservationNumber");

        RoomTypeInformation roomTypeInformation = new RoomTypeInformation("Suite");
        RoomInformation roomInformation = new RoomInformation(970,roomTypeInformation,549.);
        CustomerInformation customerInformation = new CustomerInformation(usernameTokenCreater.getUsername(),"test@email.com","Test","User");
        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber,customerInformation,roomInformation,d1,d2);
        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        jsonPath = JsonPath.from(reservationInformationJson);

        response.then().assertThat().statusCode(HttpStatus.SC_OK)
        .body("",equalTo(jsonPath.get()));

        //Viewing reservation
        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .param("reservationNumber", reservationNumber)
                .when().get(new URI("/viewreservation"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));


        //Testing availability of room at different time intervals
        //CheckIn: Prior to start. Checkout: Inside interval. Should be unavailable
        String checkInTest = "2110-10-09T17:24:56.081Z";
        String checkOutTest = "2110-10-13T17:24:56.081Z";

        given().contentType(ContentType.JSON)
        .param("checkInDate",checkInTest)
        .and().param("checkOutDate",checkOutTest)
        .and().param("roomNumber",roomNumber)
        .accept(ContentType.JSON)
        .when().get(new URI("/getroomifavailable")).then().assertThat()
        .statusCode(HttpStatus.SC_CONFLICT);

        //CheckIn: Inside interval. Checkout: Outside interval. Should be unavailable
        checkInTest = "2110-10-12T17:24:56.081Z";
        checkOutTest = "2110-10-16T17:24:56.081Z";


        given().contentType(ContentType.JSON)
        .param("checkInDate",checkInTest)
        .and().param("checkOutDate",checkOutTest)
        .and().param("roomNumber",roomNumber)
        .accept(ContentType.JSON)
        .when().get(new URI("/getroomifavailable")).then().assertThat()
        .statusCode(HttpStatus.SC_CONFLICT);

        //Testing availability of room at different time intervals
        //Checkin: Outside interval. Checkout: Outside interval. Should be unavailable
        checkInTest = "2110-10-09T17:24:56.081Z";
        checkOutTest =  "2110-10-16T17:24:56.081Z";


        given().contentType(ContentType.JSON)
        .param("checkInDate",checkInTest)
        .and().param("checkOutDate",checkOutTest)
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
        .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);

    }
}
