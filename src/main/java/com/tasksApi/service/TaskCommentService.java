package com.tasksApi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasksApi.model.TaskComment;
import com.tasksApi.model.Tasks;
import com.tasksApi.model.Users;
import com.tasksApi.repositories.TaskCommentRepository;

@Service
public class TaskCommentService {

    @Autowired
    private TaskCommentRepository taskCommentRepository;

    public TaskComment addComment(Tasks task, String text, Users createdBy)
    {
        TaskComment taskComment = new TaskComment();
        taskComment.setTask(task);
        taskComment.setText(text);
        taskComment.setCreatedBy(createdBy);

        taskCommentRepository.save(taskComment);

        return taskComment;
	}

    public Optional<TaskComment> findComment(Integer id)
    {
        return taskCommentRepository.findById(id);
	}

    public void deleteComment(TaskComment taskComment)
    {
        taskCommentRepository.delete(taskComment);
	}
}
