package com.tasksApi.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasksApi.model.TaskStatusEnum;
import com.tasksApi.model.TaskTypeEnum;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.repositories.TaskRepository;

@Service
public class TasksService {
    
    @Autowired
    private TaskRepository taskRepository;

    public Tasks create(Tasks task, Users createdBy) {
        task.setStatus(TaskStatusEnum.open);
        task.setCreatedBy(createdBy);

        taskRepository.save(task);
        return task;
	}

    public Tasks update(Tasks task, Tasks taskRequest) {
        String title = (taskRequest.getTitle() != null) ? taskRequest.getTitle() : task.getTitle();
        task.setTitle(title);

        String description = (taskRequest.getDescription() != null) ? taskRequest.getDescription() : task.getDescription();
        task.setDescription(description);

        TaskTypeEnum type = (taskRequest.getType() != null) ? taskRequest.getType() : task.getType();
        task.setType(type);

        TaskStatusEnum status = (taskRequest.getStatus() != null) ? taskRequest.getStatus() : task.getStatus();
        task.setStatus(status);
        
        taskRepository.save(task);
        return task;
	}

    public Tasks close(Tasks task, Users closedBy) {
        task.setStatus(TaskStatusEnum.closed);

        Date date = new Date(System.currentTimeMillis());
        task.setClosedOn(date);
        task.setClosedBy(closedBy);
        
        taskRepository.save(task);
        return task;
	}

    public void delete(Tasks task) {
        taskRepository.delete(task);
	}

    public Iterable<Tasks> findAllTasks() {
        return taskRepository.findAll();
	}

    public Optional<Tasks> findOneTask(Integer id) {
        return taskRepository.findById(id);
	}
}
