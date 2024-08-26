package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.TaskAssignees;

@SuppressWarnings("rawtypes")
@Repository
public interface TaskAssigneeRepository extends CrudRepository<TaskAssignees, Integer>, QueryByExampleExecutor {
}
