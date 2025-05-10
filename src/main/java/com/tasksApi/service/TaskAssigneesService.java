package com.tasksApi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasksApi.model.TaskAssignees;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.repositories.TaskAssigneeRepository;

@Service
public class TaskAssigneesService {

    @Autowired
    private TaskAssigneeRepository taskAssigneeRepository;

    @Autowired
    private TaskHistoryService taskHistoryService;

    public TaskAssignees assign(Tasks task, Users assignedTo, Users assignedBy)
    {
        TaskAssignees taskAssignee = new TaskAssignees();
        taskAssignee.setTask(task);
        taskAssignee.setAssignedTo(assignedTo);
        taskAssignee.setAssignedBy(assignedBy);

        taskAssigneeRepository.save(taskAssignee);

        taskHistoryService.addHistory("added_assignee", "", assignedTo.getName(), assignedBy, task);

        return taskAssignee;
	}

    public void unassign(TaskAssignees taskAssignees, Users removedBy)
    {
        taskAssigneeRepository.delete(taskAssignees);

        taskHistoryService.addHistory("removed_assignee", "", taskAssignees.getAssignedTo().getName(), removedBy, taskAssignees.getTask());
    }

    public Optional<TaskAssignees> findAssignment(Integer id)
    {
        return taskAssigneeRepository.findById(id);
	}
}
