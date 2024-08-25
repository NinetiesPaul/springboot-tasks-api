package com.tasksApi.customValidations.validators;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tasksApi.requests.UsersRequest;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

	ArrayList<String> validationMessages = new ArrayList<>();

    public ArrayList<String> validate(UsersRequest user) {

        if (user.getName() == null) {
            validationMessages.add("MISSING_NAME");
        } else {
            String userName = user.getName().trim();

            if (userName.equals("")) {
                validationMessages.add("EMPTY_NAME");
            }

            try {
                Integer.parseInt(userName);
                validationMessages.add("NAME_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("Name is not integer");
            }
        }

        if (user.getEmail() == null) {
            validationMessages.add("MISSING_EMAIL");
        } else {
            String userEmail = user.getEmail().trim();

            if (userEmail.equals("")) {
                validationMessages.add("EMPTY_EMAIL");
            }

            try {
                Integer.parseInt(userEmail);
                validationMessages.add("EMAIL_NOT_STRING");
            } catch (NumberFormatException nfe) {
                System.out.println("E-Mail is not integer");
            }

            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
            Matcher matcher = pattern.matcher(userEmail);
            boolean matchFound = matcher.find();
            if (!matchFound) {
                validationMessages.add("INVALID_EMAIL");
            }
        }

        if (user.getPassword() == null) {
            validationMessages.add("MISSING_PASSWORD");
        } else {
            String userPassword = user.getPassword().trim();

            if (userPassword.equals("")) {
                validationMessages.add("EMPTY_PASSWORD");
            }
        }

        return validationMessages;
    }
}
