package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.auth.CustomUserDetails;
import com.milibrodereservas.citame.entities.Message;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import com.milibrodereservas.citame.services.*;
import com.milibrodereservas.citame.util.UtilData;
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
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/citame/messages")
@Tag(name = "Mensajes", description = "Operaciones relacionadas con la funcionalidad de mensajería")
public class MessagesController extends Base {
    @Autowired
    private MessageService service;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessService businessService;

    @GetMapping("/")
    @Operation(summary = "Lista mensajes",
            description = "Devuelve una lista de mensajes para el usuario registrado",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mensajes",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageDto.class)))),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
    })
    public ResponseEntity<List<MessageDto>> getAllowedServicesByBusiness(
            @Parameter(description = "Fecha de inicio mensajes", example = "2025-08-01")
            @RequestParam(name="fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @Parameter(description = "Devolver solo mensajes no mostrados (true)", example = "true")
            @RequestParam(name = "notShowed", required = false, defaultValue = "false")
            boolean notShowed,
            @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /messages fromDate={} notShowed={} {}", fromDate, notShowed, user.getUsername());

        List<MessageDto> reply = new ArrayList<>();
        return ResponseEntity.ok(reply);
    }

    @PutMapping("/")
    @Operation(
            summary = "Graba un mensaje",
            description = "Graba un mensaje perteneciente al usuario registrado",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos del mensaje",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageRequest.class)
                    )
            ),
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensaje guardado"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No encontrados usuarios o negocio"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @Transactional
    public ResponseEntity<Void> saveMessage(@RequestBody MessageRequest msg, @AuthenticationPrincipal CustomUserDetails user) {
        logger.info("PUT /messages {} {}", msg, user.getUsername());

        MessageDto dto = new MessageDto();
        UserDto userFrom = userService.findById(user.getUserId());
        if (userFrom == null) { // imposible
            return ResponseEntity.internalServerError().build();
        }

        if (msg.getIdAppointment() != null) {
            dto.setAppointment(appointmentService.findById(msg.getIdAppointment()));
            if (dto.getAppointment() == null) {
                return ResponseEntity.notFound().build();
            }
        }

        // idBusiness es obligatorio (puede ser origen o destino del mensaje)
        if (msg.getIdBusiness() == null) {
            return ResponseEntity.badRequest().build();
        }

        // si origen es negocio el usuario registrado debe pertenecer al negocio
        if (msg.getIdUser() != null) {
            boolean found = false;
            for (UserBusinessDto item : userFrom.getUserBusinesses()) {
                if (item.getId().getIdBusiness().equals(msg.getIdBusiness())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            UserDto userTo = userService.findById(msg.getIdUser());
            if (userTo == null) {
                return ResponseEntity.notFound().build();
            }
            dto.setUser(userTo);
            dto.setFromUser(false);
        } else {
            dto.setUser(userFrom);
            dto.setFromUser(true);
        }

        dto.setBusiness(businessService.findById(msg.getIdBusiness()));
        if (dto.getBusiness() == null) {
            return ResponseEntity.notFound().build();
        }

        if (msg.getIdUserBusiness() != null) {
            dto.setUserBusiness(userService.findById(msg.getIdUserBusiness()));
            if (dto.getUserBusiness() == null) {
                return ResponseEntity.notFound().build();
            }
        }

        if ((msg.getMessage() != null) && (msg.getMessage().length() <= UtilData.getFieldLength(Message.class, "message"))) {
            dto.setMessage(msg.getMessage());
        } else {
            return ResponseEntity.badRequest().build();
        }

        dto = service.create(dto);
        if (dto == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
