package put.poznan.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import put.poznan.backend.dto.authentication.request.AuthenticationRequest;
import put.poznan.backend.dto.authentication.response.AuthenticationResponse;
import put.poznan.backend.service.AuthenticationService;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity< AuthenticationResponse > register( @RequestBody AuthenticationRequest req ) {
        return ResponseEntity.ok( service.register( req ) );
    }

    @PostMapping("/authenticate")
    public ResponseEntity< AuthenticationResponse > authenticate( @RequestBody AuthenticationRequest req ) {
        return ResponseEntity.ok( service.authenticate( req ) );
    }
}
