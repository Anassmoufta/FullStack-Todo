package todo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import todo.model.TodoModel;

@Repository
public interface TodoRepository extends MongoRepository<TodoModel, String> {

	Optional<TodoModel> findById(String id);

	void deleteById(String id);

	List<TodoModel> findByUserId(String userId);

}
