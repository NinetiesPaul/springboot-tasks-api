package com.tasksApi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasksApi.enums.TaskStatusEnum;
import com.tasksApi.model.Task;
import com.tasksApi.model.TaskAssignees;
import com.tasksApi.model.TaskHistory;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.repositories.TaskAssigneeRepository;
import com.tasksApi.repositories.TaskHistoryRepository;
import com.tasksApi.repositories.TaskRepository;
import com.tasksApi.repositories.TasksRepository;

@Service
public class TasksService {
    
    @Autowired
    private TasksRepository tasksRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @Autowired
    private TaskAssigneeRepository taskAssigneeRepository;

    public Tasks create(Tasks task, Users createdBy)
    {
        task.setStatus(TaskStatusEnum.open.toString());
        task.setCreatedBy(createdBy);

        tasksRepository.save(task);
        return task;
	}

    public Tasks update(Tasks task, Tasks taskRequest, Users changedBy)
    {
        ArrayList<TaskHistory> taskHistories = new ArrayList<TaskHistory>();

        if (taskRequest.getTitle() != null && !taskRequest.getTitle().equals(task.getTitle())) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setField("title");
            taskHistory.setChangedFrom(task.getTitle());
            taskHistory.setChangedTo(taskRequest.getTitle());
            taskHistory.setChangedOn(null);
            taskHistory.setChangedBy(changedBy);
            taskHistory.setTask(task);
            taskHistories.add(taskHistory);

            task.setTitle(taskRequest.getTitle());
        }

        if (taskRequest.getDescription() != null && (!taskRequest.getDescription().equals(task.getDescription()))) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setField("description");
            taskHistory.setChangedFrom(task.getDescription());
            taskHistory.setChangedTo(taskRequest.getDescription());
            taskHistory.setChangedOn(null);
            taskHistory.setChangedBy(changedBy);
            taskHistory.setTask(task);
            taskHistories.add(taskHistory);

            task.setDescription(taskRequest.getDescription());
        }

        if (taskRequest.getType() != null && (!taskRequest.getType().equals(task.getType()))) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setField("type");
            taskHistory.setChangedFrom(task.getType().toString());
            taskHistory.setChangedTo(taskRequest.getType().toString());
            taskHistory.setChangedOn(null);
            taskHistory.setChangedBy(changedBy);
            taskHistory.setTask(task);
            taskHistories.add(taskHistory);

            task.setType(taskRequest.getType());
        }

        if (taskRequest.getStatus() != null && (!taskRequest.getStatus().equals(task.getStatus()))) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setField("status");
            taskHistory.setChangedFrom(task.getStatus().toString());
            taskHistory.setChangedTo(taskRequest.getStatus().toString());
            taskHistory.setChangedOn(null);
            taskHistory.setChangedBy(changedBy);
            taskHistory.setTask(task);
            taskHistories.add(taskHistory);

            task.setStatus(taskRequest.getStatus());
        }
        
        tasksRepository.save(task);

        for (TaskHistory newTaskHistory: taskHistories) {
            taskHistoryRepository.save(newTaskHistory);
        }
        return task;
	}

    public Tasks close(Tasks task, Users closedBy)
    {
        String oldStatus = task.getStatus().toString();
        task.setStatus(TaskStatusEnum.closed.toString());

        Date closedOn = new Date(System.currentTimeMillis());
        task.setClosedOn(closedOn);
        task.setClosedBy(closedBy);
        
        tasksRepository.save(task);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setField("status");
        taskHistory.setChangedFrom(oldStatus);
        taskHistory.setChangedTo(TaskStatusEnum.closed.toString());
        taskHistory.setChangedOn(closedOn);
        taskHistory.setChangedBy(closedBy);
        taskHistory.setTask(task);

        taskHistoryRepository.save(taskHistory);
        return task;
	}

    public void assign(Tasks task, Users assignedTo, Users assignedBy)
    {
        TaskAssignees taskAssignee = new TaskAssignees();
        taskAssignee.setTask(task);
        taskAssignee.setAssignedTo(assignedTo);
        taskAssignee.setAssignedBy(assignedBy);

        taskAssigneeRepository.save(taskAssignee);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setField("added_assignee");
        taskHistory.setChangedFrom("");
        taskHistory.setChangedTo(assignedTo.getName());
        taskHistory.setChangedBy(assignedBy);
        taskHistory.setChangedOn(new Date(System.currentTimeMillis()));
        taskHistory.setTask(task);

        taskHistoryRepository.save(taskHistory);
	}

    public void unassign(TaskAssignees taskAssignees, Users removedBy)
    {
        taskAssigneeRepository.delete(taskAssignees);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setField("removed_assignee");
        taskHistory.setChangedFrom("");
        taskHistory.setChangedTo(taskAssignees.getAssignedTo().getName());
        taskHistory.setChangedOn(new Date(System.currentTimeMillis()));
        taskHistory.setChangedBy(removedBy);
        taskHistory.setTask(taskAssignees.getTask());

        taskHistoryRepository.save(taskHistory);
    }

    public Optional<TaskAssignees> findAssignment(Integer id)
    {
        return taskAssigneeRepository.findById(id);
	}

    public void delete(Tasks task)
    {
        tasksRepository.delete(task);
	}

    public Iterable<Tasks> findAllTasks()
    {
        return tasksRepository.findAll();
	}

    @SuppressWarnings("unchecked")
    public Iterable<Tasks> findAllTasks(Example tasks)
    {
        return tasksRepository.findAll(tasks);
	}

    public Optional<Tasks> findOneTask(Integer id)
    {
        return tasksRepository.findById(id);
	}

    public Optional<Task> findOneTaskWithHistory(Integer id)
    {
        return taskRepository.findById(id);
	}
}
