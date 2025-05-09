package com.tasksApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Table(name = "task_comments")
@Entity(name = "task_comments")
public class TaskComment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(unique = false, length = 255, nullable = true)
    @JsonProperty("text")
	private String text;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task", referencedColumnName = "id")
    private Tasks task;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @JsonProperty("created_by")
    private Users createdBy;

    @CreationTimestamp
    @Column(name = "created_on")
    @JsonProperty("created_on")
	private Date createdOn;

    public TaskComment()
	{
	}

    public TaskComment(Integer id, String text, Tasks task, Users createdBy, Date createdOn)
	{
	}

    public Integer getId() {
        return id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Tasks getTask()
    {
        return task;
    }

    public void setTask(Tasks task)
    {
        this.task = task;
    }

    public Users getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy)
    {
        this.createdBy = createdBy;
    }

        public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }
}
