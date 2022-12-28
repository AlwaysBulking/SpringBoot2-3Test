package pl.latusikl.database.services;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import pl.latusikl.database.entity.UserEntity;
import pl.latusikl.common.ResponseStatusException;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

	String USER_ID_COLUMN_NAME = "user_id";

	Uni<Boolean> existsByUserEmail(final String email) {
		return find("user_email", email).firstResult()
										.map(Objects::nonNull);
	}

	Uni<Void> deleteByUserId(final UUID id) {
		return delete(USER_ID_COLUMN_NAME, id.toString()).onItem()
														 .invoke(deletedCount -> {
															 if (deletedCount != 1L) {
																 throw new ResponseStatusException("User with given ID not found.",
																								   404);
															 }
														 })
														 .replaceWithVoid();
	}

}
