package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import put.poznan.backend.config.jwt.JwtAuthenticationManager;
import put.poznan.backend.dto.authentication.AuthenticationResponse;
import put.poznan.backend.dto.authentication.request.AuthenticationRequest;
import put.poznan.backend.entities.User;
import put.poznan.backend.exception.UsernameTakenException;
import put.poznan.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtAuthenticationManager authenticationManager;

    public AuthenticationResponse register( AuthenticationRequest req ) {
        var user = User.builder()
                .username( req.getUsername() )
                .password( passwordEncoder.encode( req.getPassword() ) )
                .build();
        if ( userRepository.existsByUsername( req.getUsername() ) ) {
            throw new UsernameTakenException( "Username taken" );
        }
        userRepository.save( user );
        var jwtToken = jwtService.generateToken( user );
        return AuthenticationResponse.builder()
                .token( jwtToken )
                .build();
    }

    public AuthenticationResponse authenticate( AuthenticationRequest req ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( req.getUsername(), req.getPassword() )
        );
        var user = userRepository.findByUsername( req.getUsername() )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
        var jwtToken = jwtService.generateToken( user );
        return AuthenticationResponse.builder()
                .token( jwtToken )
                .build();
    }
}
