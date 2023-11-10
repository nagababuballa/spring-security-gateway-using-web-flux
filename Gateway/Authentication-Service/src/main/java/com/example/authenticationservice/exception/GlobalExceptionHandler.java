package com.example.authenticationservice.exception;

import java.util.Map;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler{

	public GlobalExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
			ApplicationContext applicationContext,ServerCodecConfigurer codecConfigurer) {
		super(errorAttributes, resources, applicationContext);
		this.setMessageReaders(codecConfigurer.getReaders());
		this.setMessageWriters(codecConfigurer.getWriters());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		// TODO Auto-generated method stub
		return RouterFunctions.route(RequestPredicates.all(), this::renderException);
	}

	private Mono<ServerResponse> renderException(ServerRequest serverRequest) {
		Map<String, Object> error = this.getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
		error.remove("status");
        error.remove("requestId");
        return ServerResponse.status(HttpStatus.SC_BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
	}

}
