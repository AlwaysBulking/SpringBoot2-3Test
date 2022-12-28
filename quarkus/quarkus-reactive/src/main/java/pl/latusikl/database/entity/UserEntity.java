package pl.latusikl.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user_data")
@Data
@NoArgsConstructor
public class UserEntity {

	@Id
	@Column(name = "user_id", insertable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID userId;
	@Column(name = "user_email")
	private String userEmail;
	@Column(name = "password")
	private String password;
	@Column(name = "user_name")
	private String name;
	@Column(name = "user_surname")
	private String surname;
}
