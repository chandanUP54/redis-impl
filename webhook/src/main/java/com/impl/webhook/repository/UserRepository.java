package com.impl.webhook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impl.webhook.modal.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}

