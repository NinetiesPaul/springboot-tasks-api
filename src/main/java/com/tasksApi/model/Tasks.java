package com.tasksApi.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "task")
@Entity
public class Tasks {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Title is mandatory")
    @Column(unique = false, length = 200, nullable = false)
	private String title;

    @NotBlank(message = "Description is mandatory")
    @Column(unique = false, length = 200, nullable = true)
	private String description;

    @NotNull(message = "Type is mandatory")
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
	private TaskTypeEnum type;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
	private TaskStatusEnum status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @JsonProperty("created_by")
    private Users createdBy;

    @CreationTimestamp
    @Column(name = "created_on")
    @JsonProperty("created_on")
	private Date createdOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "closed_by", referencedColumnName = "id")
    @JsonProperty("closed_by")
    private Users closedBy;

    @Column(name = "closed_on")
    @JsonProperty("closed_on")
	private Date closedOn;

    public Tasks()
	{
	}

    public Tasks(Integer id, String title, String description, TaskTypeEnum type, TaskStatusEnum status, Users createdBy, Date createdOn, Users closedBy, Date closedOn)
	{
	}

    public Integer getId() {
        return id;
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

    public TaskTypeEnum getType()
    {
        return type;
    }

    public void setType(TaskTypeEnum type)
    {
        this.type = type;
    }

    public TaskStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(TaskStatusEnum status)
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
