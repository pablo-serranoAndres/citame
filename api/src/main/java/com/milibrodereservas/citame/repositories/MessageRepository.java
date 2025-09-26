package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
