package com.nxtweb.supareel.ping;

import com.nxtweb.supareel.ping.dto.StatusAuthenticatedResponse;
import com.nxtweb.supareel.ping.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "ping")
@Tag(name = "Ping")
public class PingController {
    @GetMapping("/ping")
    public ResponseEntity<StatusResponse> ping() {
        return new ResponseEntity<>(
                new StatusResponse("SUCCESS", "server is running"),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/ping/authenticated")
    @PreAuthorize("hasRole('view_profile')")
    public ResponseEntity<StatusAuthenticatedResponse> pingAuthenticated(Authentication connectedUser) {
        return new ResponseEntity<>(

                new StatusAuthenticatedResponse("SUCCESS", connectedUser),
                HttpStatus.ACCEPTED);
    }
}
