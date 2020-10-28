package hotel.reservation.apitesting.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomUpdate;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import hotel.reservation.apitesting.datafortests.UsernameTokenCreator;
import hotel.reservation.apitesting.mapper.MyObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AdminTest {
    private MyObjectMapper mapper = new MyObjectMapper();

    private UsernameTokenCreator usernameTokenCreator = UsernameTokenCreator.getInstance();

    @Test
    public void testGetUsersNotAdmin() throws URISyntaxException {

        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testGetUsersAdmin() throws JsonProcessingException, URISyntaxException, ParseException {
        UserInformation userInformation = new UserInformation();
        userInformation.setUserID(2085);
        userInformation.setUsername("cooluser");
        userInformation.setEmail("cooluser@gmail.com");
        userInformation.setFirstName("Joe");
        userInformation.setLastName("Bob");

        String userInformationJson = mapper.writeValueAsString(userInformation);
        JsonPath jsonPath = JsonPath.from(userInformationJson);

        String token = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("content.get(0)",equalTo(jsonPath.get()));
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

        RoomUpdate roomUpdate = new RoomUpdate(roomType,price+5);
        String json = mapper.writeValueAsString(roomUpdate);

        RoomInformation roomInformation = new RoomInformation(roomNumber,roomType,price+5);
        String roomInformationJson = mapper.writeValueAsString(roomInformation);
        jsonPath = JsonPath.from(roomInformationJson);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().put(new URI("/services/rooms/" + roomNumber))
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));

        roomUpdate = new RoomUpdate(roomType,price);
        json = mapper.writeValueAsString(roomUpdate);

        roomInformation = new RoomInformation(roomNumber,roomType,price);
        roomInformationJson = mapper.writeValueAsString(roomInformation);
        jsonPath = JsonPath.from(roomInformationJson);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().put(new URI("/services/rooms/" + roomNumber))
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));
    }


}
