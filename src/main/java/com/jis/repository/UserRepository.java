package com.jis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jis.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}