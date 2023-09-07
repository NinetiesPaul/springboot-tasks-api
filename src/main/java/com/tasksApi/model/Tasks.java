package com.tasksApi.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

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

@Table(name = "task")
@Entity
public class Tasks {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Title is mandatory")
    @Column(unique = false, length = 200, nullable = false)
	private String title;

    @Column(unique = false, length = 200, nullable = true)
	private String description;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
	private TaskTypeEnum type;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
	private TaskStatusEnum status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Users createdBy;

    @CreationTimestamp
    @Column(name = "created_on")
	private Date createdOn;

    @Column(name = "closed_on")
	private Date closedOn;

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

    public Date getStartedOn()
    {
        return createdOn;
    }

    public void setStartedOn(Date createdOn)
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
}
