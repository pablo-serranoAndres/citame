package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.auth.CustomUserDetails;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import com.milibrodereservas.citame.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/citame")
@Tag(name = "Operación", description = "Operaciones relacionadas con la funcionalidad de usuario de la aplicación")
public class OperationController extends Base {
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ParametersService parameterService;
    @Autowired
    private TimetableService timetableService;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/services/{business}")
    @Operation(summary = "Lista servicios disponibles",
            description = "Devuelve una lista de servicios disponibles de un negocio para el usuario registrado",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de servicios encontrados",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceReply.class)))),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Negocio no encontrado", content = @Content)
    })
    public ResponseEntity<List<ServiceReply>> getAllowedServicesByBusiness(
            @PathVariable String business, @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /services/{} {}", business, user.getUsername());
        List<ServiceDto> services = serviceService.getAllowedServices(business, user.getUserId());
        List<ServiceReply> reply = new ArrayList<>();
        for (ServiceDto item : services) {
            reply.add(new ServiceReply(item));
        }
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/service_parameters/{idService}")
    @Operation(summary = "Lista parámetros servicio",
            description = "Devuelve una lista con los parámetros a editar al reservar un servicio",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de parámetros",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceParameterReply.class)))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "400", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<List<ServiceParameterReply>> getParametersByService(
            @PathVariable Long idService, @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /service_parameters/{} {}", idService, user.getUsername());

        List<ParameterServiceDto> dtos = parameterService.getParametersByService(idService);
        List<ServiceParameterReply> reply = new ArrayList<>();
        for (ParameterServiceDto item : dtos) {
            ServiceParameterReply param = new ServiceParameterReply();
            param.loadFromObject(item.getParameter());
            param.setDefaultValue(parameterService.setDefaultValue(item, user.getUserId()));
            param.setRequired(item.getRequired());
            reply.add(param);
        }
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/service_availability/{idService}")
    @Operation(summary = "Lista citas disponibles",
            description = "Devuelve una lista de horarios disponibles de un servicio",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de horarios",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceAvailabilityReply.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "400", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<ServiceAvailabilityReply> getAvailabilityByService(
            @PathVariable Long idService, @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /service_availability/{} {}", idService, user.getUsername());
        ServiceAvailabilityReply reply = serviceService.getAvailability(idService);
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/timetable/{business}")
    @Operation(
            summary = "Obtiene los horarios del negocio",
            description = "Obtiene los horarios en el rango de fechas",
            parameters = {
                    @Parameter(
                            name = "startDate",
                            description = "Fecha de inicio (o única) del rango de horarios",
                            required = true,
                            example = "2025-08-01",
                            schema = @Schema(type = "string", format = "date")
                    ),
                    @Parameter(
                            name = "endDate",
                            description = "Fecha de fin del rango de horarios. Si no se especifica se devuelve solo una fecha",
                            required = false,
                            example = "2025-08-31",
                            schema = @Schema(type = "string", format = "date")
                    )
            },
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horarios negocio en las fechas solicitadas",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TimetableReply.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos o faltantes",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
    })
    public ResponseEntity<List<TimetableReply>> getTimetableByBusiness(
            @PathVariable String business,
            @RequestParam(required = true) String startDate,
            @RequestParam(required = false) String endDate,
            @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /getTimetableByBusiness/{} {} - {} {} ", business,
                startDate, endDate, user.getUsername());
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new ValidationException("startDate could not be parsed to a date (use format yyyy-mm-dd)",
                    ValidationException.FORMAT_FIELD_BAD, "startDate");
        }
        try {
            end = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new ValidationException("endDate could not be parsed to a date (use format yyyy-mm-dd)",
                    ValidationException.FORMAT_FIELD_BAD, "endDate");
        }
        if (start.compareTo(end) > 0) {
            throw new ValidationException("endDate cannot be earlier than startDate",
                    ValidationException.FORMAT_FIELD_BAD, "startDate|endDate");
        }
        if (businessRepository.findByIdString(business).orElse(null) == null) {
            throw new ValidationException("business " + business + " not found",
                    ValidationException.NOT_FOUND, "business");
        }

        List<TimetableReply> reply = timetableService.getSchedules(business, start, end);
        return ResponseEntity.ok(reply);
    }

    @PutMapping("/appointment/put")
    @Operation(
            summary = "Graba una cita",
            description = "Graba una cita con los datos pasados en el body Json",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos a guardar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentRequest.class)
                    )
            ),
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita guardada",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AppointmentPutReply.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos o faltantes",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
    })
    @Transactional
    public ResponseEntity<AppointmentPutReply> putAppointment(@RequestBody AppointmentRequest appointment, @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("PUT /appointment/put {} {}", appointment, user.getUsername());

        // Preparando objeto cita...
        AppointmentDto dto = new AppointmentDto();
        ServiceDto service = serviceService.getServiceById(appointment.getIdService());
        dto.setService(service);
        dto.setUser(new UserDto(user.getUserId()));

        // Comprobar que la cita esta disponible
        ServiceAvailabilityReply availability = serviceService.getAvailability(appointment.getIdService());
        LocalDate bookingDate = null;
        LocalTime bookingTime = null;
        LocalTime endTime = null;
        for (Iterator<ServiceAvailabilityItem> itDate = availability.getAvailability().iterator();
             (bookingDate == null) && itDate.hasNext(); ) {
            ServiceAvailabilityItem item = itDate.next();
            if (!item.getDate().isBefore(appointment.getBookingDate())) {
                for (Iterator<ServiceAvailabilityDetail> itDetail = item.getDetails().iterator();
                     (bookingDate == null) && itDetail.hasNext(); ) {
                    ServiceAvailabilityDetail detail = itDetail.next();
                    if (item.getDate().isAfter(appointment.getBookingDate()) ||
                    !detail.getStart().isBefore(appointment.getStartTime())) {
                        bookingDate = item.getDate();
                        bookingTime = detail.getStart();
                        endTime = detail.getEnd();
                    }
                }
            }
        }
        dto.setBookingDate(bookingDate);
        dto.setStartTime(bookingTime);
        dto.setEndTime(endTime);
        LocalTime limitTime = appointment.getStartTime().plusMinutes(service.effectiveBookingStep());
        if (bookingDate.isAfter(appointment.getBookingDate()) ||
                (limitTime.isAfter(bookingTime) && bookingTime.isAfter(limitTime))) { // fuera de rango
            AppointmentPutReply reply = new AppointmentPutReply(dto, user);
            reply.setAvailability(availability.getAvailability());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reply);
        }

        // Guardar registro en appointments
        dto = appointmentService.create(dto);

        // Guardar registros en parameters_service_value y parameters_user_value
        if (appointment.getParametersValues() == null) {
            appointment.setParametersValues(new ArrayList<>());
        }
        List<ParameterServiceDto> params = parameterService.getParametersByService(appointment.getIdService());
        for (ParameterServiceDto param : params) {
            ParameterValueRequest pv = null;
            for (ParameterValueRequest pvr : appointment.getParametersValues()) {
                if (pvr.getIdParameter().equals(param.getParameter().getId())) {
                    pv = pvr;
                    break;
                }
            }
            if (pv != null) {
                parameterService.saveParameterServiceValue(dto.getId(), param.getParameter().getId(),
                        pv.getValue());
                if (param.getStoreUserParam() != null) {
                    parameterService.saveParameterUserValue(user.getUserId(), param.getParameter().getId(),
                            pv.getValue());
                }
                appointment.getParametersValues().remove(pv);
            } else if (param.getRequired()) {
                throw new ValidationException("Parámetro " + param.getParameter().getName() + " es obligatorio",
                        ValidationException.REQUIRED_VALUE,
                        "parametersValues['" + param.getParameter().getName() + "']");
            }
        }
        if (!appointment.getParametersValues().isEmpty()) {
            String fields = "";
            for (ParameterValueRequest item : appointment.getParametersValues()) {
                if (StringUtils.isNotBlank(fields)) {
                    fields += ", ";
                }
                ParameterDto param = parameterService.getParameterById(item.getIdParameter());
                fields += (param != null) && (param.getBusiness().getId().equals(service.getBusiness().getId())) ?
                        param.getName() : item.getIdParameter().toString();
            }
            throw new ValidationException("Parámetros no disponibles para servicio",
                    ValidationException.NOT_FOUND_PARAM, fields);
        }

        AppointmentPutReply reply = new AppointmentPutReply(dto, user);

        return ResponseEntity.ok(reply);
    }
}
