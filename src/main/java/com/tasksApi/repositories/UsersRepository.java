package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.tasksApi.model.Tasks;

public interface UsersRepository extends CrudRepository<Tasks, Integer> {
    
}
