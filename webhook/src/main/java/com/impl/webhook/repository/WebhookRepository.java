package com.impl.webhook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.impl.webhook.modal.WebhookEvent;

public interface WebhookRepository extends JpaRepository<WebhookEvent, Long> {
}
