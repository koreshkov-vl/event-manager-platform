package dev.sorokin.eventnotificator.controller;

import dev.sorokin.eventnotificator.controller.dto.RequestNotificationIdsDto;
import dev.sorokin.eventnotificator.controller.dto.UnreadNotificationDto;
import dev.sorokin.eventnotificator.domain.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<UnreadNotificationDto>> getNotifications() {
        var unreadNotification = notificationService.getUnreadNotifications();
        return ResponseEntity.ok()
                .body(unreadNotification);
    }

    @PostMapping
    public ResponseEntity<Void> markNotifications(@Valid @RequestBody RequestNotificationIdsDto request) {
        notificationService.markNotificationsAsRead(request.notificationIds());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
