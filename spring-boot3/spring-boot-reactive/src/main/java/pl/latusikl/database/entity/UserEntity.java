package pl.latusikl.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_data")
@Data
@NoArgsConstructor
public class UserEntity {
	@Id
	@Column("user_id")
	private UUID userId;
	@Column("user_email")
	private String userEmail;
	@Column("password")
	private String password;
	@Column("user_name")
	private String name;
	@Column("user_surname")
	private String surname;

}
