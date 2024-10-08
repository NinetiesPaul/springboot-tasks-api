package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.Task;

@SuppressWarnings("rawtypes")
@Repository
public interface TaskRepository extends CrudRepository<Task, Integer>, QueryByExampleExecutor {
}
