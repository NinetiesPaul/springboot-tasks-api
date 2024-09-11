package com.tasksApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name = "task_assignees", uniqueConstraints = { @UniqueConstraint(columnNames = { "task", "assigned_to" }) })
@Entity(name = "task_assignees")
public class TaskAssignees {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task", referencedColumnName = "id")
    private Tasks task;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    @JsonProperty("assigned_to")
    private Users assignedTo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_by", referencedColumnName = "id")
    @JsonProperty("assigned_by")
    private Users assignedBy;

    public TaskAssignees()
	{
	}

    public TaskAssignees(Integer id, Tasks task, Users ownedBy)
	{
	}

    public Integer getId() {
        return id;
    }

    public Tasks getTask()
    {
        return task;
    }

    public void setTask(Tasks task)
    {
        this.task = task;
    }

    public Users getAssignedTo()
    {
        return assignedTo;
    }

    public void setAssignedTo(Users assignedTo)
    {
        this.assignedTo = assignedTo;
    }

    public Users getAssignedBy()
    {
        return assignedBy;
    }

    public void setAssignedBy(Users assignedBy)
    {
        this.assignedBy = assignedBy;
    }
}
