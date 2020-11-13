package hotel.reservation.apitesting.tests.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishapirov.hotelapi.services.user.domain.UserInformation;
import com.ishapirov.hotelapi.services.user.domain.UserSignupInformation;
import hotel.reservation.apitesting.mapper.MyObjectMapper;
import hotel.reservation.apitesting.testdata.UsernameTokenCreator;
import hotel.reservation.apitesting.tests.setup.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class UserTests extends BaseClass {

    private final MyObjectMapper mapper = new MyObjectMapper();
    private final UsernameTokenCreator usernameTokenCreator = UsernameTokenCreator.getInstance();

    @Test
    public void signupTest() throws JsonProcessingException, URISyntaxException {
        UserSignupInformation userSignupInformation = new UserSignupInformation(usernameTokenCreator.getNewUsername(),"Coolpass-123","test@email.com","Test","User");

        createUser(userSignupInformation);
    }


    @Test
    public void badPasswordSignupTest() throws JsonProcessingException, URISyntaxException {
        UserSignupInformation userSignupInformation = new UserSignupInformation("RandomName","123456","test@email.com","Test","User");
        createUserError(userSignupInformation);
    }

    @Test
    public void getUserTest() throws JsonProcessingException, URISyntaxException {
        UserInformation userInformation = new UserInformation(2085,"cooluser","cooluser@gmail.com","Joe","Bob");
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        getUser(userInformation,headers);
    }

    @Test
    public void getOtherUserNotAdminTest() throws URISyntaxException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        getUserNotAdmin("admin",headers);
    }

    @Test
    public void testGetUsersNotAdmin() throws URISyntaxException {
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        getUsersNotAdmin(headers);
    }

    @Test
    public void testGetUsersAdmin() throws JsonProcessingException, URISyntaxException {
        UserInformation userInformation = new UserInformation(2085,"cooluser","cooluser@gmail.com","Joe","Bob");
        String token = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        getUsers(0,userInformation,headers);
    }

    @Test
    public void testUpdateUser() throws JsonProcessingException, URISyntaxException {
        UserInformation userInformation = new UserInformation(2085,"cooluser","cooluser@gmail.com","Carl","Bob");
        String token = usernameTokenCreator.getExistingUserToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        updateUser(userInformation,headers);
        getUser(userInformation,headers);

        userInformation.setFirstName("Joe");

        updateUser(userInformation,headers);
        getUser(userInformation,headers);
    }

    public void createUser(UserSignupInformation userSignupInformation) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(userSignupInformation);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("username",equalTo(userSignupInformation.getUsername()),
                        "email",equalTo(userSignupInformation.getEmail()),
                        "firstName",equalTo(userSignupInformation.getFirstName()),
                        "lastName",equalTo(userSignupInformation.getLastName()));
    }

    public void createUserError(UserSignupInformation userSignupInformation) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(userSignupInformation);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .accept(ContentType.JSON)
                .when().post(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    public void getUser(UserInformation userInformation,Map<String,String> headers) throws URISyntaxException, JsonProcessingException {
        String userInformationJson = mapper.writeValueAsString(userInformation);
        JsonPath jsonPath = JsonPath.from(userInformationJson);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .when().get(new URI("/services/users/" + userInformation.getUsername()))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));
    }

    public void getUserNotAdmin(String username,Map<String,String> headers) throws URISyntaxException {

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .when().get(new URI("/services/users/" + username))
                .then()
                .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    public void getUsers(Integer index,UserInformation userInformation,Map<String,String> headers) throws JsonProcessingException, URISyntaxException {
        String userInformationJson = mapper.writeValueAsString(userInformation);
        JsonPath jsonPath = JsonPath.from(userInformationJson);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("content.get("+index+")",equalTo(jsonPath.get()));
    }

    public void getUsersNotAdmin(Map<String,String> headers) throws URISyntaxException {
        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(new URI("/services/users"))
                .then()
                .assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    private void updateUser(UserInformation userInformation, Map<String, String> headers) throws JsonProcessingException, URISyntaxException {
        String json = mapper.writeValueAsString(userInformation);
        JsonPath jsonPath = JsonPath.from(json);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .headers(headers)
                .when().put(new URI("/services/users/" + userInformation.getUsername()))
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("",equalTo(jsonPath.get()));
    }

    private void deleteUser(String username) throws URISyntaxException {
        String token = usernameTokenCreator.getExistingAdminToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", token);

        given()
                .contentType(ContentType.JSON)
                .headers(headers)
                .when().delete(new URI("/services/users/" + username))
                .then()
                .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

}
