package com.connect.websocket_application.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.connect.websocket_application.modal.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
