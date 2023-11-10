package com.example.authenticationservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import com.example.authenticationservice.model.User;
import reactor.core.publisher.Mono;

@Component
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByUsername(String username);
}
