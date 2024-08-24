package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.Tasks;

@SuppressWarnings("rawtypes")
@Repository
public interface TasksRepository extends CrudRepository<Tasks, Integer>, QueryByExampleExecutor {
}
