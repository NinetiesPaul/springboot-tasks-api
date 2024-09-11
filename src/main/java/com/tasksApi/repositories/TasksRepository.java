package com.tasksApi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.Tasks;

@SuppressWarnings("rawtypes")
@Repository
public interface TasksRepository extends CrudRepository<Tasks, Integer>, QueryByExampleExecutor {

    @Query("FROM task t LEFT JOIN task_assignees ta ON ta.task = t.id WHERE ta.id IS NULL")
    public Iterable<Tasks> hasNoAssignees();

    @Query("FROM task t LEFT JOIN task_assignees ta ON ta.task = t.id WHERE ta.id IS NOT NULL")
    public Iterable<Tasks> hasAssignees();
}
