package pl.latusikl.database.services;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import pl.latusikl.database.entity.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
	Mono<Boolean> existsByUserEmail(String email);
}
