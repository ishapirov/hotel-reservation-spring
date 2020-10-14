package com.ishapirov.hotelreservation.hotelclasses;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ishapirov.hotelapi.formdata.CustomerSignupInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable{
	private static final long serialVersionUID = -8250240976528578480L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="customer_id")
	private Integer customerID;
	@Column(unique = true)
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;

	public Customer(CustomerSignupInformation customerSignupInformation) {
		this.username = customerSignupInformation.getUsername();
		this.password = customerSignupInformation.getPassword();
		this.email = customerSignupInformation.getEmail();
		this.firstName = customerSignupInformation.getFirstName();
		this.lastName = customerSignupInformation.getLastName();
	}
}
