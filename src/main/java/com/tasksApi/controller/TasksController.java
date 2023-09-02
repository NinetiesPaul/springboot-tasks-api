package com.tasksApi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tasksApi.model.Tasks;
import com.tasksApi.service.TasksService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/task")
public class TasksController {
	
	private TasksService tasksService;

	public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }
	
	@GetMapping(path = "/list")
	public ResponseEntity<Iterable<Tasks>> findAll() {
		Iterable<Tasks> allTasks = tasksService.findAllTasks();
		return new ResponseEntity<>(allTasks, HttpStatus.OK);
	}
	
	@GetMapping(path = "/view/{id}")
	public ResponseEntity<Tasks> findOne(@PathVariable("id") Integer id) {
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (optionalTask.isPresent()) {
			return new ResponseEntity<>(optionalTask.get(), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(path = "/create")
	public ResponseEntity<Tasks> create(@Valid @RequestBody Tasks task) throws Exception {

		if (task.getTitle() == null) {
			throw new Exception("Field {title} is mandatory");
		}

		Tasks taskCreated = tasksService.create(task);
		return new ResponseEntity<>(taskCreated, HttpStatus.OK);
	}
	
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Tasks> update(@Valid @RequestBody Tasks task, @PathVariable("id") Integer id) {
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Tasks taskCreated = tasksService.update(optionalTask.get(), task);
		return new ResponseEntity<>(taskCreated, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Boolean> remove(@PathVariable("id") Integer id) {
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		tasksService.delete(optionalTask.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
    public Map<String, String> handleException(Exception error) {
		Map<String, String> errors = new HashMap<>();
		errors.put("error", error.getMessage());
        return errors;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
