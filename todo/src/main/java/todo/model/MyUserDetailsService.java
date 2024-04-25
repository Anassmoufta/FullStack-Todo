package todo.model;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import todo.repositories.UserTodoRepository;

public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserTodoRepository userTodoRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<User> userOptional = userTodoRepository.findByUsername(username);

	    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    return new org.springframework.security.core.userdetails.User(
	            user.getUsername(),
	            user.getPassword(),
	            new ArrayList<>()
	    );
	}


}
