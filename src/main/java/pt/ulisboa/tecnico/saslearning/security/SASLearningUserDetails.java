package pt.ulisboa.tecnico.saslearning.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SASLearningUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4993302296712805483L;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Collection<? extends GrantedAuthority> authorities;

	public SASLearningUserDetails(String username, String password,
			String firstName, String lastName,
			Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
