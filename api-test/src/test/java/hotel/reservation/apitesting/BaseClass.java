package hotel.reservation.apitesting;

import static io.restassured.RestAssured.*;

import java.net.URI;
import java.net.URISyntaxException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;

import hotel.reservation.apitesting.testobjects.CustomerCredentials;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BaseClass {
    private static String username;
    private static String token;
    private static String existingUserToken;

    @BeforeClass
    public static void setUp() {
        baseURI = "http://localhost";
        port = 8080;
        basePath = "";

        int length = 32;
        boolean useLetters = true;
        boolean useNumbers = true;
        username = RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String getUsername() {
        return username;
    }

    public static String getToken() throws JsonProcessingException, URISyntaxException {
        if (token == null)
            token = generateToken(username);
        return token;
    }

    public static String getExistingToken() throws JsonProcessingException, URISyntaxException {
        if (existingUserToken == null)
            existingUserToken = generateToken("cooluser");
        return existingUserToken;
    }

    private static String generateToken(String username) throws URISyntaxException, JsonProcessingException {
        CustomerCredentials testCustomer = new CustomerCredentials(username,"123456");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testCustomer);
        
        Response response = given()
        .contentType(ContentType.JSON)
        .body(json)
        .accept(ContentType.JSON)
        .when()
        .post(new URI("/authenticate"));

        JsonPath JsonPath = response.jsonPath();
        String token = JsonPath.get("token");
        return "Bearer " + token;
    }

   
}
