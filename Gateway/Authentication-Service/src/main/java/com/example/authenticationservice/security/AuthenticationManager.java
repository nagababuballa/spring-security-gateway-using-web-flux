package com.example.authenticationservice.security;

import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.authenticationservice.handler.JwtService;
import reactor.core.publisher.Mono;
import static com.example.authenticationservice.util.Constant.AUTHORITIES_KEY;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JwtService jwtService;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		String username;
		Logger log = LogManager.getLogger(AuthenticationManager.class);
		try {
			username = jwtService.extractUsername(authToken);
		} catch (Exception e) {
			log.error("exception is---",e);
			log.info("username is null");
			username = null;
		}
		if (username != null && ! jwtService.isTokenExpired(authToken)) {
			System.out.println("authToken--"+authToken);
			Claims claims = jwtService.extractAllClaims(authToken);
			List<String> roles = claims.entrySet().stream()
					.filter(t->t.getKey().equals(AUTHORITIES_KEY))
					.map(t->String.valueOf(t.getValue())).collect(Collectors.toList());
			List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
            SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, authorities));
			return Mono.just(auth);
		} else {
			return Mono.empty();
		}
	}
}
