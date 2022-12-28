package pl.latusikl.database.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@MappedEntity("user_data")
public class UserEntity {

	@Id
	@GeneratedValue(GeneratedValue.Type.AUTO)
	@MappedProperty("user_id")
	private UUID userId;
	@MappedProperty("user_email")
	private String userEmail;
	@MappedProperty("password")
	private String password;
	@MappedProperty("user_name")
	private String name;
	@MappedProperty("user_surname")
	private String surname;

}
