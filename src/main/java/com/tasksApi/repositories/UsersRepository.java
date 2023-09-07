package com.tasksApi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.tasksApi.model.Users;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    Users findByUsername(String username);
}
