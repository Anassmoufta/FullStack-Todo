package todo.repositories;



import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import todo.model.User;


@Repository
public interface UserTodoRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

}
