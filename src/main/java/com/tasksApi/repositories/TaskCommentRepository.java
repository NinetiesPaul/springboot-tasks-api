package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.TaskComment;

@SuppressWarnings("rawtypes")
@Repository
public interface TaskCommentRepository extends CrudRepository<TaskComment, Integer>, QueryByExampleExecutor {
}
