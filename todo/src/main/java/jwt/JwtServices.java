package jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;
import java.util.Date;


import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtServices {

//  @Value("${jwt.secret}")
  private String secretKey = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

//  @Value("${jwt.expiration}") // Default expiration is set to 1 hour (3600 seconds)
  private Long expiration = (long) 3600;
	
	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public String generateToken(String username) {
		
        if (secretKey == null || expiration == null) {
            throw new IllegalStateException("JWT configuration is not properly initialized.");
        }
        
		return Jwts.builder()
			.setSubject(username)
			.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
			.signWith(getKey(), SignatureAlgorithm.HS512)
			.compact();
	}
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
	}

}