package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.Business;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.BusinessDto;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BusinessService extends Base {
    @Autowired
    BusinessRepository repo;

    public BusinessDto findById(Long id) {
        try {
            Business entity = repo.findById(id).orElseThrow();
            return new BusinessDto(entity);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
