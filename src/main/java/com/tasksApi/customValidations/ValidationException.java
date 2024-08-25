package com.tasksApi.customValidations;

import java.util.ArrayList;

public class ValidationException extends Exception{

	ArrayList<String> validationMessages = new ArrayList<>();

    public ValidationException(ArrayList<String> messages)
	{
		this.validationMessages = messages;
	}

    public void setValidationMessages(ArrayList<String> messages)
    {
        this.validationMessages = messages;
    }

    public ArrayList<String> getValidationMessages()
    {
        return validationMessages;
    }
}
