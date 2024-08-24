package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.TaskHistory;

@SuppressWarnings("rawtypes")
@Repository
public interface TaskHistoryRepository extends CrudRepository<TaskHistory, Integer>, QueryByExampleExecutor {
}
