package com.tasksApi.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasksApi.enums.TaskStatusEnum;
import com.tasksApi.model.Task;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.repositories.TaskRepository;
import com.tasksApi.repositories.TasksRepository;

@Service
public class TasksService {
    
    @Autowired
    private TasksRepository tasksRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskHistoryService taskHistoryService;

    public Tasks create(Tasks task, Users createdBy)
    {
        task.setStatus(TaskStatusEnum.open.toString());
        task.setCreatedBy(createdBy);

        tasksRepository.save(task);
        return task;
	}

    public Tasks update(Tasks task, Tasks taskRequest, Users changedBy)
    {
        if (taskRequest.getTitle() != null && !taskRequest.getTitle().equals(task.getTitle())) {
            taskHistoryService.addHistory("title", task.getTitle(), taskRequest.getTitle(), changedBy, task);
            task.setTitle(taskRequest.getTitle());
        }

        if (taskRequest.getDescription() != null && (!taskRequest.getDescription().equals(task.getDescription()))) {
            taskHistoryService.addHistory("description", task.getDescription(), taskRequest.getDescription(), changedBy, task);
            task.setDescription(taskRequest.getDescription());
        }

        if (taskRequest.getType() != null && (!taskRequest.getType().equals(task.getType()))) {
            taskHistoryService.addHistory("type", task.getType().toString(), taskRequest.getType().toString(), changedBy, task);
            task.setType(taskRequest.getType());
        }

        if (taskRequest.getStatus() != null && (!taskRequest.getStatus().equals(task.getStatus()))) {
            taskHistoryService.addHistory("status", task.getStatus().toString(), taskRequest.getStatus().toString(), changedBy, task);
            task.setStatus(taskRequest.getStatus());
        }
        
        tasksRepository.save(task);
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

        taskHistoryService.addHistory("status",oldStatus, TaskStatusEnum.closed.toString(), closedBy, task);
        return task;
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
    public Iterable<Tasks> findAllTasks(@SuppressWarnings("rawtypes") Example tasks)
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

    public Iterable<Tasks> findTasksWithoutAssignees(Example tasks)
    {
        return tasksRepository.hasNoAssignees();
	}

    public Iterable<Tasks> findTasksWithAssignees(Example tasks)
    {
        return tasksRepository.hasAssignees();
	}
}
