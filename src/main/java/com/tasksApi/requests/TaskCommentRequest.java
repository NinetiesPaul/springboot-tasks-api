package com.tasksApi.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskCommentRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	
	@JsonProperty("text")
	private String text;

	public TaskCommentRequest()
	{
	}

	public TaskCommentRequest(String text)
	{
		this.setText(text);
	}

	public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
