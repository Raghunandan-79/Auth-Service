package com.example.authservice.repository;

import org.springframework.stereotype.Repository;

import com.example.authservice.entities.UserInfo;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {
    public UserInfo findByUsername(String username);
}
