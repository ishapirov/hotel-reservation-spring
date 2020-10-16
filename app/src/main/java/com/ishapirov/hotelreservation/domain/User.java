package com.ishapirov.hotelreservation.domain;

import java.io.Serializable;

import javax.persistence.*;

import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{
	private static final long serialVersionUID = -8250240976528578480L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id")
	private Integer userID;
	private String username;
	private String email;
	private String firstName;
	private String lastName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="user_sec", referencedColumnName = "username")
	private UserSecurity userSecurity;


	public User(UserSignupInformation userSignupInformation) {
		this.email = userSignupInformation.getEmail();
		this.username = userSignupInformation.getUsername();
		this.firstName = userSignupInformation.getFirstName();
		this.lastName = userSignupInformation.getLastName();
	}


}
