package com.tasksApi.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tasksApi.config.JwtTokenUtil;
import com.tasksApi.customValidations.ValidationException;
import com.tasksApi.customValidations.validators.UserValidator;
import com.tasksApi.requests.AuthenticationRequest;
import com.tasksApi.requests.UsersRequest;
import com.tasksApi.responses.JwtResponse;
import com.tasksApi.service.UsersService;

@RestController
@CrossOrigin
public class UsersController {
    @Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UsersService usersService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> generateAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

		final UserDetails userDetails = usersService.loadUserByUsername(authenticationRequest.getUserName());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(handleSuccess(new JwtResponse(token)));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@Valid @RequestBody UsersRequest user) throws Exception, ValidationException, SQLIntegrityConstraintViolationException {

		UserValidator registerRequestValidator = new UserValidator();
		ArrayList<String> validationMessages = registerRequestValidator.validate(user);

		if (validationMessages.size() > 0) {
			throw new ValidationException(validationMessages);
		}

		return ResponseEntity.ok(usersService.save(user));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	public Map<String, Object> handleSuccess(JwtResponse token)
	{
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("token", token.getToken());
        return response;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Map<String, Object> handleValidationExceptions(SQLIntegrityConstraintViolationException ex) {
		Map<String, Object> response = new HashMap<>();

		ArrayList<String> message = new ArrayList<>();
		message.add("EMAIL_ALREADY_TAKEN");
        
		response.put("message", message);
		response.put("success", false);

        return response;
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
    public Map<String, Object> handleValidationException(ValidationException exception)
	{
		Map<String, Object> response = new HashMap<>();

		//ArrayList<String> validationMessages = registerRequestException.getValidationMessages();
        
		response.put("message", exception.getValidationMessages());
		response.put("success", false);

        return response;
    }
}
