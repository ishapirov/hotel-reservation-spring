package hotel.reservation.apitesting.testdata;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UsernameTokenCreator {

    private static UsernameTokenCreator instance = new UsernameTokenCreator(864_000_000,  "73AEBDFC5F371E413F8F1EF0E0E388E8CFB72B4BCCE4C1E023C85301779E456E");
    private final long expiration;

    private final String newUsername;
    private final String newToken;
    private final String existingUsername;
    private final String existingUserToken;
    private final String existingAdminToken;

    private UsernameTokenCreator(long expiration, String secret) {
        int length = 32;
        boolean useLetters = true;
        boolean useNumbers = true;
        this.expiration = expiration;

        newUsername = RandomStringUtils.random(length, useLetters, useNumbers);
        Map<String,Object> claims = new HashMap<>();

        newToken = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject(newUsername)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        existingUsername = "cooluser";

        existingUserToken = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject(existingUsername)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, "73AEBDFC5F371E413F8F1EF0E0E388E8CFB72B4BCCE4C1E023C85301779E456E")
                .compact();

        existingAdminToken = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject("admin")
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, "73AEBDFC5F371E413F8F1EF0E0E388E8CFB72B4BCCE4C1E023C85301779E456E")
                .compact();
    }

    public String getTokenFromUsername(String username){
        Map<String,Object> claims = new HashMap<>();
        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, "73AEBDFC5F371E413F8F1EF0E0E388E8CFB72B4BCCE4C1E023C85301779E456E")
                .compact();
    }

    public static UsernameTokenCreator getInstance(){
        return instance;
    }
    
    public String getNewUsername() {
        return newUsername;
    }

    public String getNewToken() {
        return newToken;
    }

    public String getExistingUsername(){
        return existingUsername;
    }

    public String getExistingUserToken() {
        return existingUserToken;
    }

    public String getExistingAdminToken() {
        return existingAdminToken;
    }

}
