package hotel.reservation.apitesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import static org.hamcrest.Matchers.*;

public class ApiTest extends BaseClass {
    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testGetExistingRoom() throws URISyntaxException, JsonProcessingException {

        RoomInformation roomInformation = new RoomInformation(1985,"Single",214.);
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        given()
        .accept(ContentType.JSON)
        .when().get(new URI("/services/rooms/1985"))
        .then().assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body(  "",equalTo(jsonPath.get()));

    }

    @Test
    public void testGetNonExistingRoom() throws URISyntaxException {
        given()
        .accept(ContentType.JSON)
        .when().get(new URI("/services/rooms/120"))
        .then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testGetAllRooms() throws URISyntaxException, JsonProcessingException {
        RoomInformation roomInformation = new RoomInformation(1985,"Single",214.);
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        given().accept(ContentType.JSON)
                .when().get(new URI("/services/rooms"))
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK).body("content.size()", equalTo(25),
                "totalElements", equalTo(100),
                "size", equalTo(25),
                "last", equalTo(false),
                "content.get(0)", equalTo(jsonPath.get()));

        roomInformation = new RoomInformation(2035,"Suite",397.);
        roomInformationJson = mapper.writeValueAsString(roomInformation);
        jsonPath = JsonPath.from(roomInformationJson);

        given().accept(ContentType.JSON)
                .param("pageNumber","1")
                .param("size","50")
                .when().get(new URI("/services/rooms"))
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK).body("content.size()", equalTo(50),
                "content.get(0)", equalTo(jsonPath.get()),
                "last",equalTo(true));

        given().accept(ContentType.JSON)
                .param("pageNumber","4")
                .when().get(new URI("/services/rooms"))
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK).body("empty", equalTo(true));
    }

    @Test
    public void etagTest() throws URISyntaxException {
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
    public void testGetRoomsByType() throws URISyntaxException, JsonProcessingException {
        RoomInformation roomInformation = new RoomInformation(2061,"Penthouse",814.);
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        given().contentType(ContentType.JSON)
        .param("roomType","Penthouse")
        .accept(ContentType.JSON)
        .when().get(new URI("/services/rooms")).then().assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("content.size()",equalTo(25),
                "content.get(1)",equalTo(jsonPath.get()));
        
    }

    @Test
    public void testAvailableRoomsNoType() throws URISyntaxException, JsonProcessingException {
        //There is a reservation set for this time for room 1986, so the second room in the returned list should be room 1987.
        String d1 ="2110-10-10T17:24:56.081Z";
        String d2 ="2110-10-11T17:24:56.081Z";

        RoomInformation roomInformation = new RoomInformation(1987,"Single",199.);
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        given().contentType(ContentType.JSON)
        .param("checkInDate",d1)
        .and().param("checkOutDate",d2)
        .accept(ContentType.JSON)
        .when().get(new URI("/services/rooms")).then().assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("content.size()",equalTo(24),
                "content.get(1)",equalTo(jsonPath.get()));
        
    }

    @Test
    public void testAvailableRoomsByType() throws URISyntaxException, JsonProcessingException {
        //There is a reservation set for this time for room 1986, so the third room in the returned list should be room 1988.
        String type = "Single";
        String d1 ="2110-10-10T17:24:56.081Z";
        String d2 ="2110-10-11T17:24:56.081Z";

        RoomInformation roomInformation = new RoomInformation(1988,"Single",179.);
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        JsonPath jsonPath = JsonPath.from(roomInformationJson);

        given().contentType(ContentType.JSON)
        .param("checkInDate",d1)
        .and().param("checkOutDate",d2)
        .and().param("roomType",type)
        .accept(ContentType.JSON)
        .when().get(new URI("/services/rooms")).then().assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("content.size()",equalTo(24),
                "content.get(2)",equalTo(jsonPath.get()));
    }

    
}
