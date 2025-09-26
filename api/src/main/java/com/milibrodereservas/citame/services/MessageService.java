package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.Message;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.MessageDto;
import com.milibrodereservas.citame.repositories.MessageRepository;
import com.milibrodereservas.citame.util.UtilMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService extends Base {
    @Autowired
    private MessageRepository repo;
    @Autowired
    private UtilMail utilMail;

    public MessageDto create (MessageDto message) {
        Message entity = new Message(message);
        entity = repo.save(entity);
        if (entity.getFromUser()) { // envio hacia negocio
            if (entity.getUserBusiness() != null) {
                entity = sendMailAndUpdate(entity, entity.getUserBusiness().getMailMessages(),
                        entity.getUserBusiness().getEmail(), entity.getUser().getName());
            } else {
                entity = sendMailAndUpdate(entity, entity.getBusiness().getMailMessages(),
                        entity.getBusiness().getEmail(), entity.getUser().getName());
            }
        } else { // envio hacia usuario
            entity = sendMailAndUpdate(entity, entity.getUser().getMailMessages(),
                    entity.getUser().getEmail(), entity.getBusiness().getName());
        }
        if (entity != null) {
            return new MessageDto(entity);
        } else {
            return null;
        }
    }
    private Message sendMailAndUpdate(Message entity, boolean mailMessage, String email, String nameFrom) {
        if (mailMessage && (email != null)) {
            utilMail.sendEmail(email,
                    "Mensaje de " + nameFrom + " a través de citame",
                    entity.getMessage());
            entity.setEmailed(LocalDateTime.now());
            entity = repo.save(entity); // update emailed
        }
        return entity;
    }
}
