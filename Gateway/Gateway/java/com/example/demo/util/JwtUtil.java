package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.example.demo.filter.InvalidTokenException;
import com.google.common.base.Function;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.util.Constant.AUTHORITIES_KEY;
import static com.example.demo.util.Constant.*;

@Component
public class JwtUtil {

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public List<String> extractAuthorities(String token) {
        return extractClaim(token, t->t.entrySet().stream()
				.filter(p->p.getKey().equals(AUTHORITIES_KEY))
				.map(q->String.valueOf(q.getValue())).collect(Collectors.toList()));
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, String usernameStr) {
        final String username = extractUsername(token);
        return (username.equals(usernameStr) && !isTokenExpired(token));
    }
    
    public void validate(final String token) throws InvalidTokenException{
    	try {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    	}catch(Exception e) {
    		throw new InvalidTokenException("Invalid Token......!");
    	}
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SIGNING_KEY);
        System.out.println("Keys.hmacShaKeyFor(keyBytes)----"+Keys.hmacShaKeyFor(keyBytes));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
