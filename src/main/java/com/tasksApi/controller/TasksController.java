package com.tasksApi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import com.tasksApi.model.TaskStatusEnum;
import com.tasksApi.model.TaskTypeEnum;
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
	
	private TasksService tasksService;
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public TasksController(TasksService tasksService)
	{
        this.tasksService = tasksService;
    }
	
	@GetMapping(path = "/list")
	public ResponseEntity<Map<String, Object>> findAll(@RequestParam(required = false) String status, @RequestParam(required = false) String type, @RequestParam(required = false) Integer created_by)
	{
		Tasks task = new Tasks();

		if (status != null) {
			task.setStatus(TaskStatusEnum.valueOf(status));
		}

		if (type != null) {
			task.setType(TaskTypeEnum.valueOf(type));
		}

		if (created_by != null) {
			Optional<Users> optionalCreatedBy = usersService.findById(created_by);
			if (!optionalCreatedBy.isPresent()) {
				return new ResponseEntity<>(handleResponseWithMessage("USER_NOT_FOUND", false), HttpStatus.NOT_FOUND);
			}
			
			task.setCreatedBy(optionalCreatedBy.get());
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
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (optionalTask.isPresent()) {
			return new ResponseEntity<>(handleSuccess(optionalTask.get()), HttpStatus.OK);
		}

		return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(path = "/create")
	public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Tasks task, HttpServletRequest request) throws Exception
	{
		String jwtToken = request.getHeader("Authorization").substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		Users user = usersService.findByName(username);

		Tasks taskCreated = tasksService.create(task, user);
		return new ResponseEntity<>(handleSuccess(taskCreated), HttpStatus.OK);
	}
	
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Tasks task, @PathVariable("id") Integer id) throws Exception
	{
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
		}

		if (task.getStatus() == TaskStatusEnum.closed) {
			throw new Exception("Invalid operation: use PUT /api/task/close/{id} to close a task");
		}

		if (optionalTask.get().getStatus() == TaskStatusEnum.closed) {
			throw new Exception("Invalid operation: cannot update a closed task");
		}

		Tasks taskUpdated = tasksService.update(optionalTask.get(), task);
		return new ResponseEntity<>(handleSuccess(taskUpdated), HttpStatus.OK);
	}

	@PutMapping(path = "/close/{id}")
	public ResponseEntity<Map<String, Object>> close(HttpServletRequest request, @PathVariable("id") Integer id) throws Exception
	{
		Optional<Tasks> optionalTask = tasksService.findOneTask(id);

		if (!optionalTask.isPresent()) {
			return new ResponseEntity<>(handleResponseWithMessage("TASK_NOT_FOUND", false), HttpStatus.NOT_FOUND);
		}

		if (optionalTask.get().getStatus() == TaskStatusEnum.closed) {
			throw new Exception("Invalid operation: cannot close a closed task");
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

    public Map<String, Object> handleResponseWithMessage(String msg, Boolean success)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("success", success);
		response.put("msg", msg);
        return response;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
    public Map<String, String> handleException(Exception error)
	{
		Map<String, String> errors = new HashMap<>();
		errors.put("error", error.getMessage());
        return errors;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
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
    }
}
