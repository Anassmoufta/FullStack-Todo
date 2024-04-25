package todo.controllers;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Auth.AuthResponse;
import Auth.AuthUsername;
import Auth.LoginRequest;
import Auth.RegisterRequest;
import jwt.JwtServices;
import todo.model.User;
import todo.repositories.UserTodoRepository;


@Configuration
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserTodoRepository userTodoRepository;
	
	

	@GetMapping("/getall")
	public List<User> fetchAll() {
		return this.userTodoRepository.findAll();
	}
	
	
	
	
	
	@GetMapping("/get")
	public ResponseEntity<?> GetUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		AuthUsername authUsername = new AuthUsername(username);
		return new ResponseEntity<>(authUsername, HttpStatus.OK);

	}
	

	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody RegisterRequest request) {
		if (request.getUsername().length() <= 20 && request.getUsername() != null && request.getEmail() != null 
				&& request.getPassword() != null && request.getPassword().length() <= 20 
				&& request.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			
			if (userTodoRepository.findByUsername(request.getUsername()).isEmpty() && userTodoRepository.findByEmail(request.getEmail()).isEmpty()) {
				User user = new User();
				user.setUsername(request.getUsername());
				user.setEmail(request.getEmail());
				user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
				user = this.userTodoRepository.save(user);
				
				return new ResponseEntity<>(user, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}

	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
		if (this.userTodoRepository.findById(id).isPresent()) {
			user.setId(id);
			return new ResponseEntity<>(this.userTodoRepository.save(user), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		if (this.userTodoRepository.findById(id).isPresent()) {
			this.userTodoRepository.deleteById(id);
			return new ResponseEntity<> (HttpStatus.OK);
		}
		else {
			return new ResponseEntity<> (HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/finduser/{id}")
	public ResponseEntity<?> findUser(@RequestParam String id) {
		if (this.userTodoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>(this.userTodoRepository.findAll(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Bean
	JwtServices jwtServices() {
		return new JwtServices();
	};
	
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
		
		Optional<User> userOptional = this.userTodoRepository.findByUsername(request.getUsername());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
				String token = jwtServices().generateToken(request.getUsername());
				AuthResponse response = new AuthResponse(token);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	public UserTodoRepository getUserTodoRepository() {
		return userTodoRepository;
	}
	public void setUserTodoRepository(UserTodoRepository userTodoRepository) {
		this.userTodoRepository = userTodoRepository;
	}
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	
}
