package com.tasksApi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tasksApi.config.JwtTokenUtil;
import com.tasksApi.customValidations.ValidationException;
import com.tasksApi.customValidations.validators.TaskValidator;
import com.tasksApi.enums.TaskStatusEnum;
import com.tasksApi.model.Task;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.service.TasksService;
import com.tasksApi.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/task")
@CrossOrigin
public class TasksController {
	
	@Autowired
	private TasksService tasksService;
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@GetMapping(path = "/list")
	public ResponseEntity<Map<String, Object>> findAll(
		@RequestParam(required = false) String status,
		@RequestParam(required = false) String type,
		@RequestParam(required = false) Integer created_by) throws ValidationException
	{
		Tasks task = new Tasks();
		ArrayList<String> validationMessages = new ArrayList<>();

		if (status != null) {
			task.setStatus(status);
		}

		if (type != null) {
			task.setType(type);
		}

		if (created_by != null) {
			Optional<Users> optionalCreatedBy = usersService.findById(created_by);
			if (!optionalCreatedBy.isPresent()) {
				validationMessages.add("USER_NOT_FOUND");
			} else {
				task.setCreatedBy(optionalCreatedBy.get());
			}
		}

		TaskValidator taskCreationValidator = new TaskValidator();
		validationMessages.addAll(taskCreationValidator.validateOnListingOrUpdate(task));

		if (validationMessages.size() > 0) {
			throw new ValidationException(validationMessages);
		}

		Example<Tasks> exampleTasks = Example.of(task);
		Iterable<Tasks> allTasks = tasksService.findAllTasks(exampleTasks);

		Map<String, Object> result = new HashMap<>();
		result.put("tasks", allTasks);
		result.put("total", IterableUtils.size(allTasks));


		return new ResponseEntity<>(handleSuccess(result), HttpStatus.OK);
	}
	
	@GetMapping(path = "/view/{id}")
	public ResponseEntity<Map<String, Object>> findOne(@PathVariable("id") Integer id)
	{
		Optional<Task> optionalTask = tasksService.findOneTaskWithHistory(id);

		if (optionalTask.isPresent()) {
			return new ResponseEntity<>(handleSuccess(optionalTask.get()), HttpStatus.OK);
		}

		return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(path = "/create")
	public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Tasks task, HttpServletRequest request) throws Exception
	{
		TaskValidator taskCreationValidator = new TaskValidator();
		ArrayList<String> validationMessages = taskCreationValidator.validate(task);

		if (validationMessages.size() > 0) {
			throw new ValidationException(validationMessages);
		}

		String jwtToken = request.getHeader("Authorization").substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		Users user = usersService.findByName(username);

		Tasks taskCreated = tasksService.create(task, user);
		return new ResponseEntity<>(handleSuccess(taskCreated), HttpStatus.OK);
	}
	
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Tasks task, @PathVariable("id") Integer id, HttpServletRequest request) throws Exception
	{
		TaskValidator taskValidator = new TaskValidator();
		ArrayList<String> validationMessages = taskValidator.validateOnListingOrUpdate(task);

		if (validationMessages.size() > 0) {
			throw new ValidationException(validationMessages);
		}

		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
		}

		if (task.getStatus() != null && task.getStatus().equals(TaskStatusEnum.closed.toString())) {
			return new ResponseEntity<>(handleResponseWithMessage("CAN_NOT_UPDATE_TO_CLOSE", false), HttpStatus.BAD_REQUEST);
		}

		if (optionalTask.get().getStatus().equals(TaskStatusEnum.closed.toString())) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_CLOSED", false), HttpStatus.BAD_REQUEST);
		}
		
		String jwtToken = request.getHeader("Authorization").substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		Users changedBy = usersService.findByName(username);

		Tasks taskUpdated = tasksService.update(optionalTask.get(), task, changedBy);
		return new ResponseEntity<>(handleSuccess(taskUpdated), HttpStatus.OK);
	}

	@PutMapping(path = "/close/{id}")
	public ResponseEntity<Map<String, Object>> close(HttpServletRequest request, @PathVariable("id") Integer id) throws Exception
	{
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
		}

		if (optionalTask.get().getStatus().equals(TaskStatusEnum.closed.toString())) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_ALREADY_CLOSED", false), HttpStatus.BAD_REQUEST);
		}

		String jwtToken = request.getHeader("Authorization").substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		Users user = usersService.findByName(username);

		Tasks taskClosed = tasksService.close(optionalTask.get(), user);
		return new ResponseEntity<>(handleSuccess(taskClosed), HttpStatus.OK);
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Map<String, Object>> remove(@PathVariable("id") Integer id)
	{
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);
		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
		}

		tasksService.delete(optionalTask.get());
		return new ResponseEntity<>(handleResponseWithMessage("Task id '" + id + "' was deleted", true), HttpStatus.OK);
	}

    public Map<String, Object> handleSuccess(Object data)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("data", data);
        return response;
    }

    public Map<String, Object> handleResponseWithMessage(String message, Boolean success)
	{
		ArrayList<String> messages = new ArrayList<>();
		messages.add(message);

		Map<String, Object> response = new HashMap<>();
		response.put("success", success);
		response.put("message", messages);
        return response;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
    public Map<String, Object> handleValidationException(ValidationException exception)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("message", exception.getValidationMessages());
		response.put("success", false);
        return response;
    }

	/*@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex)
	{
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }*/
}
