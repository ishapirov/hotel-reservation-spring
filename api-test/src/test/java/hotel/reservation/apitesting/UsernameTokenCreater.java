package hotel.reservation.apitesting;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UsernameTokenCreater {

    private static UsernameTokenCreater instance = new UsernameTokenCreater(864_000_000,  "73AEBDFC5F371E413F8F1EF0E0E388E8CFB72B4BCCE4C1E023C85301779E456E");
    private final String username;
    private final String token;
    private final String existingToken;

    private UsernameTokenCreater(long expiration,String secret) {
        int length = 32;
        boolean useLetters = true;
        boolean useNumbers = true;
        username = RandomStringUtils.random(length, useLetters, useNumbers);
        Map<String,Object> claims = new HashMap<>();

        token = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        existingToken = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject("cooluser")
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS256, "73AEBDFC5F371E413F8F1EF0E0E388E8CFB72B4BCCE4C1E023C85301779E456E")
                .compact();
    }

    public static UsernameTokenCreater getInstance(){
        return instance;
    }
    
    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getExistingToken() {
        return existingToken;
    }
}
