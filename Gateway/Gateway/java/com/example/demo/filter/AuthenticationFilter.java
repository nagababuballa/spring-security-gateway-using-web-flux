package com.example.demo.filter;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import com.example.demo.dto.ApiResponse;
import com.example.demo.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    
    @Autowired
    JwtUtil jwtUtil;
    
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config){

        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }
                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token != null && token.startsWith("Bearer ")) {
                	token = token.substring(7);
                }
                try {
                  jwtUtil.validate(token);
                }catch (InvalidTokenException e) {
                    new ApiResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Invalid Token Entered", null);
                }catch (Exception e) {
                    System.out.println("Ecception occured while parsing the token..."+token+",...,"+e);
                }
            }
            return chain.filter(exchange);
            });
    }
    
    public static class Config {

}
}
