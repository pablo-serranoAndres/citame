package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.auth.CustomUserDetails;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import com.milibrodereservas.citame.services.ServiceService;
import com.milibrodereservas.citame.services.TimetableService;
import com.milibrodereservas.citame.services.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/citame")
@Tag(name = "Operación", description = "Operaciones relacionadas con la funcionalidad de usuario de la aplicación")
public class OperationController extends Base {
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private TimetableService timetableService;
    @Autowired
    private BusinessRepository businessRepository;

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

    @GetMapping("/service_availability/{idService}")
    @Operation(summary = "Lista citas disponibles",
            description = "Devuelve una lista de horarios disponibles de un servicio",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de horarios",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceAvailabilityReply.class)))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<List<ServiceAvailabilityReply>> getAvailabilityByService(
            @PathVariable Long idService, @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /service_availability/{} {}", idService, user.getUsername());
        List<ServiceAvailabilityReply> reply = new ArrayList<>();
        reply.add(new ServiceAvailabilityReply(LocalDate.now()));
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
}
