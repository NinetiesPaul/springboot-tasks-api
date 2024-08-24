package com.tasksApi.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "task_history")
@Entity
public class TaskHistory {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(unique = false, length = 200, nullable = false)
	private String field;

    @Column(unique = false, length = 200, nullable = true)
    @JsonProperty("changed_from")
	private String changedFrom;

    @Column(unique = false, length = 200, nullable = true)
    @JsonProperty("changed_to")
	private String changedTo;

    @CreationTimestamp
    @Column(name = "changed_on")
    @JsonProperty("changed_on")
	private Date changedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "changed_by", referencedColumnName = "id")
    @JsonProperty("changed_by")
    private Users changedBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task", referencedColumnName = "id")
    private Tasks task;

    public TaskHistory()
	{
	}

    public TaskHistory(Integer id, String field, String changedFrom, String changedTo, Date changedOn, Users changedBy, Tasks task)
	{
	}

    public Integer getId() {
        return id;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public String getChangedFrom()
    {
        return changedFrom;
    }

    public void setChangedFrom(String changedFrom)
    {
        this.changedFrom = changedFrom;
    }

    public String getChangedTo()
    {
        return changedTo;
    }

    public void setChangedTo(String changedTo)
    {
        this.changedTo = changedTo;
    }

    public Date getChangedOn()
    {
        return changedOn;
    }

    public void setChangedOn(Date changedOn)
    {
        this.changedOn = changedOn;
    }

    public Users getChangedBy()
    {
        return changedBy;
    }

    public void setChangedBy(Users changedBy)
    {
        this.changedBy = changedBy;
    }

    public Tasks getTask()
    {
        return task;
    }

    public void setTask(Tasks task)
    {
        this.task = task;
    }
}
