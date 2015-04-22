package pt.ulisboa.tecnico.saslearning.signup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
	@Size(min=2, max=30)
	private String username;
	
	@NotNull
	@Size(min=6)
	private String password;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
