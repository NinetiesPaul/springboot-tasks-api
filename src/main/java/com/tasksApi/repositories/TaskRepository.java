package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tasksApi.model.Tasks;

@Repository
public interface TaskRepository extends CrudRepository<Tasks, Integer> {
    
    
}
