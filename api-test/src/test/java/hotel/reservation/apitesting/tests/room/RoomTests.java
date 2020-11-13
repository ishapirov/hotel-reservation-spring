package hotel.reservation.apitesting.tests.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.ishapirov.hotelapi.services.room.domain.RoomInformation;
import com.ishapirov.hotelapi.services.room.domain.RoomUpdate;
import com.ishapirov.hotelapi.services.room.paramvalidation.RoomsCriteria;
import hotel.reservation.apitesting.testdata.UsernameTokenCreator;
import hotel.reservation.apitesting.tests.helperclasses.PageExpectedData;
import hotel.reservation.apitesting.tests.setup.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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

public class RoomTests extends BaseClass {
    private final ObjectMapper mapper = new ObjectMapper();
    private final StdDateFormat stdDateFormat = new StdDateFormat();
    private UsernameTokenCreator usernameTokenCreator = UsernameTokenCreator.getInstance();

    @Test
    public void testGetExistingRoom() throws URISyntaxException, JsonProcessingException {
        RoomInformation roomInformation = new RoomInformation(1985,"Single",214.);
        verifyRoom(roomInformation);
    }

    @Test
    public void testGetNonExistingRoom() throws URISyntaxException {
       verifyRoomNotExists(120);
    }

    @Test
    public void testGetAllRooms() throws URISyntaxException, JsonProcessingException {
        RoomInformation roomInformation = new RoomInformation(2035,"Suite",397.);
        RoomsCriteria roomsCriteria = new RoomsCriteria();
        roomsCriteria.setSize(50);
        roomsCriteria.setPageNumber(1);
        PageExpectedData pageExpectedData = new PageExpectedData(2,false,50);

        verifyGetRooms(0,roomInformation,roomsCriteria,pageExpectedData);

    }

    @Test
    public void testGetRoomsPageNumberExceedsPagesWithContent() throws URISyntaxException {
        RoomsCriteria roomsCriteria = new RoomsCriteria();
        roomsCriteria.setPageNumber(4);
        roomsCriteria.setSize(25);
        PageExpectedData pageTest = new PageExpectedData(4,false,0);
        verifyGetRoomsEmpty(roomsCriteria,pageTest);
    }


    @Test
    public void testGetRoomsByType() throws URISyntaxException, JsonProcessingException {
        RoomInformation roomInformation = new RoomInformation(2061,"Penthouse",814.);
        RoomsCriteria roomsCriteria = new RoomsCriteria();
        roomsCriteria.setPageNumber(0);
        roomsCriteria.setSize(25);
        roomsCriteria.setRoomType("Penthouse");
        PageExpectedData pageExpectedData = new PageExpectedData(1,false,25);

        verifyGetRooms(1,roomInformation,roomsCriteria,pageExpectedData);
    }

    @Test
    public void testAvailableRoomsNoType() throws URISyntaxException, JsonProcessingException, ParseException {
        //There is a reservation set for this time for room 1986, so the second room in the returned list should be room 1987.
        Date checkIn  = stdDateFormat.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-11T17:24:56.081Z");

        RoomInformation roomInformation = new RoomInformation(1987,"Single",199.);
        RoomsCriteria roomsCriteria = new RoomsCriteria();
        roomsCriteria.setPageNumber(0);
        roomsCriteria.setSize(25);
        roomsCriteria.setCheckInDate(checkIn);
        roomsCriteria.setCheckOutDate(checkOut);
        PageExpectedData pageExpectedData = new PageExpectedData(4,true,25);
        verifyGetRooms(1,roomInformation,roomsCriteria,pageExpectedData);
    }

    @Test
    public void testAvailableRoomsByType() throws URISyntaxException, JsonProcessingException, ParseException {
        //There is a reservation set for this time for room 1986, so the third room in the returned list should be room 1988.
        String type = "Single";
        Date checkIn  = stdDateFormat.parse("2110-10-10T17:24:56.081Z");
        Date checkOut = stdDateFormat.parse("2110-10-11T17:24:56.081Z");

        RoomInformation roomInformation = new RoomInformation(1988,"Single",179.);
        RoomsCriteria roomsCriteria = new RoomsCriteria();
        roomsCriteria.setPageNumber(0);
        roomsCriteria.setSize(25);
        roomsCriteria.setCheckInDate(checkIn);
        roomsCriteria.setCheckOutDate(checkOut);
        roomsCriteria.setRoomType(type);
        PageExpectedData pageExpectedData = new PageExpectedData(1,false,24);
        verifyGetRooms(2,roomInformation,roomsCriteria,pageExpectedData);
    }

    @Test
    public void testRoomETag() throws URISyntaxException {
        Response response = given().accept(ContentType.JSON)
                .when().get(new URI("/services/rooms"));

        String etagValue = response.getHeader("ETag");
        Map<String,String> headers = new HashMap<>();
        headers.put("If-None-Match",etagValue);

        given().accept(ContentType.JSON)
                .headers(headers)
                .when().get(new URI("/services/rooms"))
                .then().assertThat()
                .statusCode(HttpStatus.SC_NOT_MODIFIED);
    }



    @Test
    public void testUpdateRoomAdmin() throws JsonProcessingException, URISyntaxException {
        String token = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        int roomNumber = 1985;
        Response response = given()
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomNumber));

        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        double price = jsonPath.getDouble("roomPrice");
        String roomType = jsonPath.getString("roomType");

        RoomInformation roomInformationIncreasePrice = new RoomInformation(roomNumber,roomType,price+5);
        RoomUpdate roomUpdateIncreasePrice = new RoomUpdate(roomType,price+5);
        updateRoom(roomInformationIncreasePrice,roomUpdateIncreasePrice,headers);

        RoomInformation roomInformationDecreasePrice = new RoomInformation(roomNumber,roomType,price);
        RoomUpdate roomUpdateDecreasePrice = new RoomUpdate(roomType,price);
        updateRoom(roomInformationDecreasePrice,roomUpdateDecreasePrice,headers);
    }

    public void verifyRoom(RoomInformation roomInformation) throws JsonProcessingException, URISyntaxException {

        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        given()
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomInformation.getRoomNumber()))
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(  "",equalTo(jsonPath.get()));
    }

    public void verifyRoomNotExists(Integer roomNumber) throws URISyntaxException {
        given()
                .accept(ContentType.JSON)
                .when().get(new URI("/services/rooms/" + roomNumber))
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    public void verifyGetRooms(Integer index, RoomInformation roomInformation, RoomsCriteria roomsCriteria, PageExpectedData pageExpectedData) throws JsonProcessingException, URISyntaxException {
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath roomJsonPath = JsonPath.from(roomInformationJson);

        RequestSpecification requestSpecification =
                given().accept(ContentType.JSON)
                .param("pageNumber",roomsCriteria.getPageNumber())
                .param("size",roomsCriteria.getSize());
        if(roomsCriteria.getRoomType() != null)
            requestSpecification.param("roomType",roomsCriteria.getRoomType());
        if(roomsCriteria.getCheckInDate() != null)
            requestSpecification = requestSpecification.param("checkInDate",stdDateFormat.format(roomsCriteria.getCheckInDate()));
        if(roomsCriteria.getCheckOutDate() != null)
            requestSpecification = requestSpecification.param("checkOutDate",stdDateFormat.format(roomsCriteria.getCheckOutDate()));


        requestSpecification.when().get(new URI("/services/rooms"))
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK).body("content.size()", equalTo(pageExpectedData.getSize()),
                "content.get(" + index + ")", equalTo(roomJsonPath.get()),
                "hasNextPage",equalTo(pageExpectedData.isNextPage()),
                "totalPages",equalTo(pageExpectedData.getTotalPages()));
    }

    public void verifyGetRoomsEmpty(RoomsCriteria roomsCriteria, PageExpectedData pageExpectedData) throws URISyntaxException {

        given().accept(ContentType.JSON)
                .param("pageNumber",roomsCriteria.getPageNumber())
                .param("size",roomsCriteria.getSize())
                .when().get(new URI("/services/rooms"))
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("content.size()", equalTo(pageExpectedData.getSize()),
                        "hasNextPage",equalTo(pageExpectedData.isNextPage()),
                        "totalPages",equalTo(pageExpectedData.getTotalPages()));
    }

    public void updateRoom(RoomInformation roomInformation,RoomUpdate roomUpdate,Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        String json = mapper.writeValueAsString(roomUpdate);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().put(new URI("/services/rooms/" + roomInformation.getRoomNumber()))
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));
    }
}
