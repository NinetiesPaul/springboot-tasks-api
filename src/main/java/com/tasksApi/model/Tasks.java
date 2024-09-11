package com.tasksApi.model;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "task")
@Entity(name = "task")
public class Tasks {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(unique = false, length = 200, nullable = false)
	private String title;

    @Column(unique = false, length = 200, nullable = true)
	private String description;

    @Column(nullable = false, length = 20)
    /*@Enumerated(EnumType.STRING)
	private TaskTypeEnum type;*/
	private String type;

    @Column(nullable = false, length = 20)
    /*@Enumerated(EnumType.STRING)
	private TaskStatusEnum status;*/
	private String status;

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

    //@OneToMany(mappedBy = "task", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OneToMany(mappedBy="task")
    @JsonProperty("assignees")
    private Set<TaskAssignees> assignees;

    public Tasks()
	{
	}

    public Tasks(Integer id, String title, String description, String type, String status, Users createdBy, Date createdOn, Users closedBy, Date closedOn)
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
