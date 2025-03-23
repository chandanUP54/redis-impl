package com.connect.websocket_application.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.connect.websocket_application.modal.WebhookEvent;


public interface WebhookRepository extends JpaRepository<WebhookEvent, Long> {
}

