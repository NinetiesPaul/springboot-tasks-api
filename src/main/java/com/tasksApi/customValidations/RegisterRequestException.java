package com.tasksApi.customValidations;

import java.util.ArrayList;

public class RegisterRequestException extends Exception{

	ArrayList<String> validationMessages = new ArrayList<>();

    public RegisterRequestException(ArrayList<String> messages)
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
