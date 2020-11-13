package hotel.reservation.apitesting.tests.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.ishapirov.hotelapi.services.reservation.domain.*;
import com.ishapirov.hotelapi.services.reservation.domain.admin.BookReservationForUser;
import hotel.reservation.apitesting.mapper.MyObjectMapper;
import hotel.reservation.apitesting.testdata.UsernameTokenCreator;
import hotel.reservation.apitesting.tests.setup.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReservationTests extends BaseClass {
    private final MyObjectMapper mapper = new MyObjectMapper();
    private final UsernameTokenCreator usernameTokenCreator = UsernameTokenCreator.getInstance();
    private final StdDateFormat stdDateFormat = new StdDateFormat();

    @Test
    public void testViewReservation() throws URISyntaxException, JsonProcessingException, ParseException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);
        StdDateFormat format = new StdDateFormat();
        Date checkIn = format.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = format.parse("2110-10-11T17:24:56.081Z");
        ReservationInformation reservationInformation = new ReservationInformation(2086, "cooluser",1986,checkIn,checkOut,false);
        viewReservation(reservationInformation,headers);
    }

    @Test
    public void testViewForbiddenReservation() throws URISyntaxException {
        String token = usernameTokenCreator.getNewToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        viewForbiddenReservation(headers);
    }


    @Test
    public void bookReservationVerifyDelete() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);
        Integer roomNumber = 2035;

        Date checkIn = stdDateFormat.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-15T17:24:56.081Z");
        BookReservation bookReservation = new BookReservation(roomNumber,checkIn,checkOut);
        int reservationNumber = bookReservation(bookReservation,headers);

        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut,false);
        ReservationBasicInformation reservationBasicInformation = new ReservationBasicInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut);

        viewReservation(reservationInformation,headers);
        viewAllReservations(reservationBasicInformation,headers);

        //CheckIn: Outside interval. Checkout: Inside interval. Should be unavailable
        String checkInParam = "2110-10-09T17:24:56.081Z";
        String checkOutParam = "2110-10-13T17:24:56.081Z";
        confirmRoomNoLongerAvailable(roomNumber,checkInParam,checkOutParam);

        //CheckIn: Inside interval. Checkout: Outside interval. Should be unavailable
        checkInParam = "2110-10-12T17:24:56.081Z";
        checkOutParam = "2110-10-16T17:24:56.081Z";
        confirmRoomNoLongerAvailable(roomNumber,checkInParam,checkOutParam);

        //CheckIn: Outside interval. Checkout: Outside interval. Should be unavailable
        checkInParam = "2110-10-09T17:24:56.081Z";
        checkOutParam =  "2110-10-16T17:24:56.081Z";
        confirmRoomNoLongerAvailable(roomNumber,checkInParam,checkOutParam);

        //CheckIn: Inside interval. Checkout: Inside interval. Should be unavailable
        checkInParam = "2110-10-09T17:24:56.081Z";
        checkOutParam =  "2110-10-16T17:24:56.081Z";
        confirmRoomNoLongerAvailable(roomNumber,checkInParam,checkOutParam);

        //Both Earlier than checkIn. Should be available
        checkInParam = "2110-10-03T17:24:56.081Z";
        checkOutParam =  "2110-10-07T17:24:56.081Z";
        confirmRoomAvailable(roomNumber,checkInParam,checkOutParam);

        deleteReservation(reservationNumber);
        viewNonExistentReservation(reservationNumber,headers);
    }

    @Test
    public void bookReservationCancelVerifyDelete() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);
        Integer roomNumber = 2035;

        Date checkIn = stdDateFormat.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-15T17:24:56.081Z");
        BookReservation bookReservation = new BookReservation(roomNumber,checkIn,checkOut);
        int reservationNumber = bookReservation(bookReservation,headers);

        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut,false);

        viewReservation(reservationInformation,headers);

        //CheckIn: Outside interval. Checkout: Outside interval. Should be unavailable
        String checkInParam = "2110-10-09T17:24:56.081Z";
        String checkOutParam =  "2110-10-16T17:24:56.081Z";
        confirmRoomNoLongerAvailable(roomNumber,checkInParam,checkOutParam);

        cancelReservation(reservationNumber,headers);

        reservationInformation.setCancelled(true);
        viewReservation(reservationInformation,headers);

        //Should be available after cancelling
        checkInParam = "2110-10-09T17:24:56.081Z";
        checkOutParam =  "2110-10-16T17:24:56.081Z";
        confirmRoomAvailable(roomNumber,checkInParam,checkOutParam);


        deleteReservation(reservationNumber);
        viewNonExistentReservation(reservationNumber,headers);
    }

    @Test
    public void bookReservationUpdateVerifyDelete() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);
        Integer roomNumber = 1986;

        Date checkIn = stdDateFormat.parse("2110-10-13T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-15T17:24:56.081Z");
        BookReservation bookReservation = new BookReservation(roomNumber,checkIn,checkOut);
        int reservationNumber = bookReservation(bookReservation,headers);

        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut,false);
        ReservationBasicInformation reservationBasicInformation = new ReservationBasicInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut);

        viewReservation(reservationInformation,headers);
        viewAllReservations(reservationBasicInformation,headers);

        //Test to make sure reservation cannot overlap itself
        checkIn = stdDateFormat.parse("2110-10-13T17:24:56.081Z");
        checkOut = stdDateFormat.parse("2110-10-17T17:24:56.081Z");
        ReservationUpdate reservationUpdate = new ReservationUpdate(roomNumber,checkIn,checkOut);
        updateReservationAvailable(reservationUpdate,reservationNumber,headers);

        //Update to date outside range
        checkIn = stdDateFormat.parse("2110-10-20T17:24:56.081Z");
        checkOut = stdDateFormat.parse("2110-10-25T17:24:56.081Z");
        reservationUpdate = new ReservationUpdate(roomNumber,checkIn,checkOut);
        updateReservationAvailable(reservationUpdate,reservationNumber,headers);

        //Attempt to update to unavailable date
        checkIn = stdDateFormat.parse("2110-10-09T17:24:56.081Z");
        checkOut = stdDateFormat.parse("2110-10-15T17:24:56.081Z");

        reservationUpdate = new ReservationUpdate(roomNumber,checkIn,checkOut);
        updateReservationUnavailable(reservationUpdate,reservationNumber,headers);

        deleteReservation(reservationNumber);
        viewNonExistentReservation(reservationNumber,headers);
    }

    @Test
    public void bookReservationForUserVerifyDelete() throws JsonProcessingException, URISyntaxException, ParseException {
        String adminToken = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> adminHeaders = new HashMap<>();
        adminHeaders.put("Authorization",adminToken);
        Integer roomNumber = 2035;

        Date checkIn = stdDateFormat.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-15T17:24:56.081Z");

        BookReservationForUser bookReservationForUser  = new BookReservationForUser(usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut);
        int reservationNumber = bookReservationForUser(bookReservationForUser,adminHeaders);

        String userToken = usernameTokenCreator.getExistingUserToken();
        Map<String,String> userHeaders = new HashMap<>();
        userHeaders.put("Authorization",userToken);

        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut,false);
        ReservationBasicInformation reservationBasicInformation = new ReservationBasicInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut);

        viewReservation(reservationInformation,userHeaders);
        viewAllReservations(reservationBasicInformation,userHeaders);

        deleteReservation(reservationNumber);
        viewNonExistentReservation(reservationNumber,userHeaders);
    }

    @Test
    public void bookReservationCancelForUserVerifyDelete() throws JsonProcessingException, URISyntaxException, ParseException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> userHeaders = new HashMap<>();
        userHeaders.put("Authorization",token);
        Integer roomNumber = 2035;

        Date checkIn = stdDateFormat.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-15T17:24:56.081Z");
        BookReservation bookReservation = new BookReservation(roomNumber,checkIn,checkOut);
        int reservationNumber = bookReservation(bookReservation,userHeaders);
        ReservationInformation reservationInformation = new ReservationInformation(reservationNumber, usernameTokenCreator.getExistingUsername(),roomNumber,checkIn,checkOut,false);

        viewReservation(reservationInformation,userHeaders);

        cancelReservationForUser(reservationNumber);

        reservationInformation.setCancelled(true);
        viewReservation(reservationInformation,userHeaders);

        deleteReservation(reservationNumber);
        viewNonExistentReservation(reservationNumber,userHeaders);
    }


    private Integer bookReservation(BookReservation bookReservation, Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(bookReservation);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/reservations"));

        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        return jsonPath.getInt("reservationNumber");
    }



    private void viewReservation(ReservationInformation reservationInformation, Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        JsonPath jsonPath = JsonPath.from(reservationInformationJson);

        given().contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/reservations/"+reservationInformation.getReservationNumber()))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));
    }

    private void viewForbiddenReservation(Map<String,String> headers) throws URISyntaxException {
        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .param("reservationNumber", "2086")
                .when().get(new URI("/services/reservations/2086"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    private void viewNonExistentReservation(Integer reservationNumber, Map<String,String> headers) throws URISyntaxException {
        given().contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/reservations/"+reservationNumber))
                .then()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    private void viewAllReservations(ReservationBasicInformation reservationInformation, Map<String,String> headers) throws URISyntaxException, JsonProcessingException {

        String reservationInformationJson = mapper.writeValueAsString(reservationInformation);
        JsonPath jsonPath = JsonPath.from(reservationInformationJson);

        given().contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .param("username", reservationInformation.getUsername())
                .when().get(new URI("/services/reservations/"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("content.get(1)", equalTo(jsonPath.get()),
                        "hasNextPage", equalTo(false),
                        "totalPages",equalTo(1));

    }

    private void confirmRoomNoLongerAvailable(Integer roomNumber,String checkIn,String checkOut) throws URISyntaxException {
        given().contentType(ContentType.JSON)
                .param("checkInDate",checkIn)
                .and().param("checkOutDate",checkOut)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomNumber)).then().assertThat()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    private void confirmRoomAvailable(Integer roomNumber,String checkIn,String checkOut) throws URISyntaxException {
        given().contentType(ContentType.JSON)
                .param("checkInDate",checkIn)
                .and().param("checkOutDate",checkOut)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomNumber)).then().assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    private void updateReservationAvailable(ReservationUpdate reservationUpdate,Integer reservationNumber, Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(reservationUpdate);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().put(new URI("/services/reservations/"+reservationNumber))
                .then().statusCode(HttpStatus.SC_OK);
    }

    private void updateReservationUnavailable(ReservationUpdate reservationUpdate,Integer reservationNumber, Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(reservationUpdate);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().put(new URI("/services/reservations/"+reservationNumber))
                .then().statusCode(HttpStatus.SC_CONFLICT);
    }

    private void deleteReservation(Integer reservationNumber) throws JsonProcessingException, URISyntaxException {
        String token = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().delete(new URI("/services/reservations/"+reservationNumber))
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private void cancelReservation(Integer reservationNumber,Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        CancelReservation cancelReservation = new CancelReservation(reservationNumber);
        String json = mapper.writeValueAsString(cancelReservation);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/reservations/cancel"))
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    private Integer bookReservationForUser(BookReservationForUser bookReservationForUser, Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(bookReservationForUser);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/reservations/admin"));

        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        return jsonPath.getInt("reservationNumber");
    }

    private void cancelReservationForUser(Integer reservationNumber) throws JsonProcessingException, URISyntaxException {
        String token = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);

        CancelReservation cancelReservation = new CancelReservation(reservationNumber);
        String json = mapper.writeValueAsString(cancelReservation);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/reservations/admin/cancel"))
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
