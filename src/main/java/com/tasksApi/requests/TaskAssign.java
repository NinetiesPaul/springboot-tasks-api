package com.tasksApi.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskAssign implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	@JsonProperty("assigned_to")
	private Integer assignedTo;

	public TaskAssign()
	{
	}

	public TaskAssign(Integer assignedTo)
	{
		this.setAssignedTo(assignedTo);
	}

	public Integer getAssignedTo()
    {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo)
    {
        this.assignedTo = assignedTo;
    }
}
