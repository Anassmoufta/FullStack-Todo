package todo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import todo.model.TodoModel;
import todo.repositories.TodoRepository;
import todoServices.todoDone;
import todoServices.todoService;

@RestController
@RequestMapping("/todo")
public class TodoController {
	
	@Autowired
	TodoRepository todoRepository;
	
	@GetMapping("/getall")
	public List<TodoModel> fetchAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = authentication.getName();
		return this.todoRepository.findByUserId(userId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addTodo(@RequestBody TodoModel request) {
		if (request.getTask().length() <= 20 && request.getTask() != null &&
				request.getDescription().length() <= 80 && request.getDescription() != null &&
						request.getDate() != null && request.getIsDone() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userId = authentication.getName();
			TodoModel todo = new TodoModel();
			todo.setTask(request.getTask());
			todo.setDescription(request.getDescription());
			todo.setDate(request.getDate());
			todo.setIsDone(request.getIsDone());
			todo.setuserId(userId);
			return new ResponseEntity<>(this.todoRepository.save(todo), HttpStatus.CREATED);	
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteTodo(@PathVariable String id) {
		if (this.todoRepository.findById(id).isPresent()) {
			this.todoRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("done/{id}")
	public ResponseEntity<?> Done(@PathVariable String id , @RequestBody todoDone request ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Optional<TodoModel> mytodos = this.todoRepository.findById(id);
		TodoModel todo = new TodoModel();
		todo.setId(id);
		todo.setTask(mytodos.get().getTask());
		todo.setDescription(mytodos.get().getDescription());
		todo.setDate(mytodos.get().getDate());
		todo.setIsDone(request.getIsDone());
		todo.setuserId(username);
		
		return new ResponseEntity<>(this.todoRepository.save(todo), HttpStatus.OK);

	}
	
	@PutMapping("update/{id}")
	public ResponseEntity<?> updateTodo(@PathVariable String id, @RequestBody todoService request) {
		if (request.getTask().length() <= 20 && request.getTask() != null &&
				request.getDescription().length() <= 80 && request.getDescription() != null &&
						request.getDate() != null && request.getIsDone() != null && 
								request.getTask().length() <= 20 && request.getTask() != null &&
										request.getDescription().length() <= 80 && request.getDescription() != null &&
												request.getDate() != null && request.getIsDone() != null) {
			
			if (this.todoRepository.findById(id).isPresent()) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String username = authentication.getName();
				TodoModel todoModel = new TodoModel();
				todoModel.setId(id);
				todoModel.setTask(request.getTask());
				todoModel.setDescription(request.getDescription());
				todoModel.setDate(request.getDate());
				todoModel.setIsDone(request.getIsDone());
				todoModel.setuserId(username);
					return new ResponseEntity<>(this.todoRepository.save(todoModel), HttpStatus.CREATED);	
				}
				else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@GetMapping("findelement/{id}")
	public ResponseEntity<?> findElement(@PathVariable String id) {
		if (this.todoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>(this.todoRepository.findById(id), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}