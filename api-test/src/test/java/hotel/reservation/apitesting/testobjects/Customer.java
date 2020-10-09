package hotel.reservation.apitesting.testobjects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
}
