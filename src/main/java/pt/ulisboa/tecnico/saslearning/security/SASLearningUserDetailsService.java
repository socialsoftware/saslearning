package pt.ulisboa.tecnico.saslearning.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.User;

public class SASLearningUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = getUserDetails(username);
		if(user == null){
			throw new UsernameNotFoundException("User with username '" + username + "' not found.");
		}
		return user;
	}

	@Atomic(mode = TxMode.READ)
	private UserDetails getUserDetails(String username) {
		Set<User> users = FenixFramework.getDomainRoot().getUserSet();
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("STUDENT"));
				return new SASLearningUserDetails(username, u.getPassword(),
						u.getFirstName(), u.getLastName(), authorities);
			}
		}
		return null;
	}

}
