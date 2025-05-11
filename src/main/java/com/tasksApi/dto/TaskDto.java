package com.tasksApi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tasksApi.model.Users;

public class TaskDto {
    private Integer id;

	private String title;

	private String description;

	private String type;

	private String status;

    @JsonProperty("created_by")
    private Users createdBy;

    @JsonProperty("created_on")
	private Date createdOn;

    @JsonProperty("closed_by")
    private Users closedBy;

    @JsonProperty("closed_on")
	private Date closedOn;
    
    public TaskDto()
	{
	}

    public TaskDto(Integer id, String title, String description, String type, String status, Users createdBy, Date createdOn, Users closedBy, Date closedOn)
	{
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public Date getClosedOn()
    {
        return closedOn;
    }

    public void setClosedOn(Date closedOn)
    {
        this.closedOn = closedOn;
    }

    public Users getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy)
    {
        this.createdBy = createdBy;
    }

    public Users getClosedBy()
    {
        return closedBy;
    }

    public void setClosedBy(Users closedBy)
    {
        this.closedBy = closedBy;
    }
}
