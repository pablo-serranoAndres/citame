package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.*;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.global.Constantes;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.*;
import com.milibrodereservas.citame.util.UtilData;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@org.springframework.stereotype.Service
public class ParametersService extends Base {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ParameterRepository parRepo;
    @Autowired
    private ParameterServiceRepository psRepo;
    @Autowired
    private ParameterUserValueRepository puvRepo;
    @Autowired
    private ParameterServiceValueRepository psvRepo;

    public ParameterDto getParameterById(Long id) {
        try {
            return new ParameterDto(parRepo.findById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<ParameterServiceDto> getParametersByService(Long idService) {
        try {
            List<ParameterServiceDto> reply = new ArrayList<>();

            Service service = serviceRepository.findById(idService).orElseThrow();

            List<ParameterService> psList = psRepo.findByIdIdServiceOrderByPosition(idService);
            for (ParameterService ps : psList) {
                final ParameterServiceDto item = new ParameterServiceDto(ps);
                reply.add(item);
            }
            return reply;
        } catch (NoSuchElementException e) {
            throw new ValidationException("Service not found", ValidationException.NOT_FOUND, "idService");
        }
    }

    public String setDefaultValue(ParameterServiceDto ps, Long idUser) {
        String value = null;
        if ((idUser != null) && (ps.getRecoverUserParam() != null)) {
            value = getValueFromUserParameter(idUser, ps.getParameter().getId());
        }
        if (value == null) {
            value = ps.getDefaultValue();
        }
        if (value == null) {
            value = ps.getParameter().getDefaultValue();
        }
        return value;
    }

    public String getValueFromUserParameter(Long idUser, Long idParameter) {
        return getValueFromUserParameter(new ParameterUserValueId(idUser, idParameter));
    }
    public String getValueFromUserParameter(ParameterUserValueId id) {
        try {
            return puvRepo.findById(id).orElseThrow().getValue();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Transactional
    public void saveParameterServiceValue(Long idAppointment, Long idParameter, String value) {
        ParameterDto param = this.getParameterById(idParameter);
        if (param == null) {
            throw new ValidationException("Parámetro no existe", ValidationException.NOT_FOUND, idParameter.toString());
        }
        if (!valueParameterValid(param, value)) {
            throw new ValidationException("Valor no válido parámetro", ValidationException.FORMAT_FIELD_BAD, param.getName());
        }
        ParameterServiceValue psv;
        ParameterServiceValueId id = new ParameterServiceValueId(idAppointment, idParameter);
        try {
            psv = psvRepo.findById(id)
                    .orElseThrow();
            psv.setModificated(LocalDateTime.now());
        } catch (NoSuchElementException e) {
            psv = new ParameterServiceValue();
            psv.setId(id);
            psv.setCreated(LocalDateTime.now());
        }
        psv.setValue(value);
        psvRepo.save(psv);
    }

    @Transactional
    public void saveParameterUserValue(Long idUser, Long idParameter, String value) {
        ParameterDto param = this.getParameterById(idParameter);
        if (param == null) {
            throw new ValidationException("Parámetro no existe", ValidationException.NOT_FOUND, idParameter.toString());
        }
        if (!valueParameterValid(param, value)) {
            throw new ValidationException("Valor no válido parámetro", ValidationException.FORMAT_FIELD_BAD, param.getName());
        }
        ParameterUserValue puv;
        ParameterUserValueId id = new ParameterUserValueId(idUser, idParameter);
        try {
            puv = puvRepo.findById(id)
                    .orElseThrow();
            puv.setModificated(LocalDateTime.now());
        } catch (NoSuchElementException e) {
            puv = new ParameterUserValue();
            puv.setId(id);
            puv.setCreated(LocalDateTime.now());
        }
        puv.setValue(value);
        puvRepo.save(puv);
    }

    public boolean valueParameterValid(Long idParam, @NotNull String value) {
        ParameterDto param = this.getParameterById(idParam);
        if (param != null) {
            return valueParameterValid(param, value);
        } else {
            return false;
        }
    }
    public boolean valueParameterValid(ParameterDto param, @NotNull String value) {
        if (value.length() > UtilData.getFieldLength(ParameterServiceValue.class, "value")) {
            return false;
        }

        final String auxValue = param.getAuxValue();
        switch (param.getTypeValue()) {
            case Parameter.TYPEVALUE_CHECK:
                return (Constantes.CHECK_FALSE.equalsIgnoreCase(value)) ||
                        Constantes.CHECK_TRUE.equalsIgnoreCase(value);
            case Parameter.TYPEVALUE_STRING:
                if (auxValue == null) {
                    return true;
                }
                try {
                    final Integer lengtMax = Integer.parseInt(auxValue);
                    return (value.length() <= lengtMax);
                } catch (NumberFormatException e) {
                    return true;
                }
            case Parameter.TYPEVALUE_INTEGER:
                return UtilData.cumpleRango(value, auxValue, true);
            case Parameter.TYPEVALUE_FLOAT:
                return UtilData.cumpleRango(value, auxValue, false);
            case Parameter.TYPEVALUE_LIST:
                int nElems = auxValue.split("\\|").length;
                return UtilData.cumpleRango(value, "[0," + nElems + ")", true);
            case Parameter.TYPEVALUE_DATE:
                try {
                    LocalDate.parse(value, DateTimeFormatter.ofPattern(auxValue));
                    return true;
                } catch (DateTimeParseException e) {
                    return false;
                }
            case Parameter.TYPEVALUE_TIME:
                try {
                    LocalTime.parse(value, DateTimeFormatter.ofPattern(auxValue));
                    return true;
                } catch (DateTimeParseException e) {
                    return false;
                }
            case Parameter.TYPEVALUE_DATETIME:
                try {
                    LocalDateTime.parse(value, DateTimeFormatter.ofPattern(auxValue));
                    return true;
                } catch (DateTimeParseException e) {
                    return false;
                }
        }
        return false;
    }
}
