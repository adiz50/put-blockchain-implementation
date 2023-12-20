package put.poznan.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.poznan.backend.dto.authentication.request.AuthenticationRequest;
import put.poznan.backend.dto.authentication.request.PasswordResetRequest;
import put.poznan.backend.dto.authentication.response.AuthenticationResponse;
import put.poznan.backend.service.AuthenticationService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register( @RequestBody AuthenticationRequest req ) {
        service.register( req );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate( @RequestBody AuthenticationRequest req ) {
        return ResponseEntity.ok( service.authenticate( req ) );
    }

    @PostMapping("/activate")
    public ResponseEntity<AuthenticationResponse> activateAccount( @RequestParam UUID code ) {
        return ResponseEntity.ok( service.activateAccount( code ) );
    }

    @PostMapping(path = "/forgotten")
    public ResponseEntity<Object> forgottenPassword( @RequestBody AuthenticationRequest req ) {
        service.forgottenPassword( req.getEmail() );
        return ResponseEntity.ok( "Check your email for further instructions" );
    }

    @PostMapping(path = "/reset")
    public ResponseEntity<AuthenticationResponse> resetPassword( @RequestBody PasswordResetRequest req ) {
        return ResponseEntity.ok( service.resetPassword( req ) );
    }
}