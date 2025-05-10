package com.tasksApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasksApi.model.TaskHistory;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.repositories.TaskHistoryRepository;

@Service
public class TaskHistoryService {

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    public void addHistory(String field, String changedFrom, String changedTo, Users changedBy, Tasks task)
    {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setField(field);
        taskHistory.setChangedFrom(changedFrom);
        taskHistory.setChangedTo(changedTo);
        taskHistory.setChangedBy(changedBy);
        taskHistory.setChangedOn(null);
        taskHistory.setTask(task);

        taskHistoryRepository.save(taskHistory);
    }
}
