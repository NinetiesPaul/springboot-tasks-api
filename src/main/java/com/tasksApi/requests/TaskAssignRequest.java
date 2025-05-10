package com.tasksApi.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskAssignRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	@JsonProperty("assigned_to")
	private String assignedTo;

	public TaskAssignRequest()
	{
	}

	public TaskAssignRequest(String assignedTo)
	{
		this.setAssignedTo(assignedTo);
	}

	public String getAssignedTo()
    {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo)
    {
        this.assignedTo = assignedTo;
    }
}
