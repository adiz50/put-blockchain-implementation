package put.poznan.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import put.poznan.backend.config.jwt.JwtAuthenticationManager;
import put.poznan.backend.dto.authentication.request.AuthenticationRequest;
import put.poznan.backend.dto.authentication.request.PasswordResetRequest;
import put.poznan.backend.dto.authentication.response.AuthenticationResponse;
import put.poznan.backend.entities.Mail;
import put.poznan.backend.entities.User;
import put.poznan.backend.entities.Verify;
import put.poznan.backend.exception.AccessDenied;
import put.poznan.backend.exception.InvalidEmail;
import put.poznan.backend.exception.UsernameTakenException;
import put.poznan.backend.repository.UserRepository;
import put.poznan.backend.repository.VerifyRepository;
import put.poznan.backend.types.ActionType;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtAuthenticationManager authenticationManager;
    private final VerifyRepository verifyRepository;
    private final MailService mailService;
    @Value("${mail.forgotten.body}")
    private String forgottenPassBody;
    @Value("${mail.forgotten.subject}")
    private String forgottenPassSubject;
    @Value("${mail.forgotten.redirect}")
    private String forgottenPassRedirect;

    public AuthenticationResponse register( AuthenticationRequest req ) {
        var user = User.builder()
                .username( req.getUsername() )
                .password( passwordEncoder.encode( req.getPassword() ) )
                .email( req.getEmail() )
                .build();
        if ( userRepository.existsByUsername( req.getUsername() ) ) {
            throw new UsernameTakenException( "Username taken" );
        }
        if ( ! EmailValidator.getInstance().isValid( req.getEmail() ) || userRepository.existsByEmail( req.getEmail() ) ) {
            throw new InvalidEmail( "Email is taken or invalid" );
        }
        if ( ! isPasswordValid( req.getPassword() ) )
            throw new RuntimeException( "Password does not fulfill the requirements" );
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

    @Transactional(rollbackOn = Throwable.class)
    public void forgottenPassword( String email ) {
        if ( ! EmailValidator.getInstance().isValid( email ) || ! userRepository.existsByEmail( email ) )
            throw new InvalidEmail( "Email is invalid or there is no user with such email" );
        Optional<User> user = userRepository.findByEmail( email );
        UUID code = UUID.randomUUID();
        Verify verify = Verify.builder()
                .id( code )
                .actionType( ActionType.FORGOTTEN_PASSWORD )
                .user( user.get() )
                .build();
        Mail mail = Mail.builder()
                .body( forgottenPassBody + "\n" + forgottenPassRedirect + code )
                .subject( forgottenPassSubject )
                .email( email )
                .build();
        verifyRepository.save( verify );
        mailService.addMail( mail );
    }

    @Transactional(rollbackOn = Throwable.class)
    public AuthenticationResponse resetPassword( PasswordResetRequest req ) {
        System.out.println( req );
        Optional<Verify> verify = verifyRepository.findById( req.getCode() );
        if ( verify.isEmpty() ) throw new AccessDenied( "Invalid code" );
        User user = verify.get().getUser();
        user.setPassword( BCrypt.hashpw( req.getPassword(), BCrypt.gensalt() ) );
        user = userRepository.save( user );
        verifyRepository.delete( verify.get() );
        var jwtToken = jwtService.generateToken( user );
        return AuthenticationResponse.builder()
                .token( jwtToken )
                .build();
    }

    /**
     * Checks wheter password is valid. <br>
     * Password must contain: <br>
     * - at least 6 characters <br>
     * - at least one digit <br>
     * - at least one special symbol <br>
     */
    private boolean isPasswordValid( String password ) {
        return password.length() >= 6 && password.matches( ".*\\d.*" )
                && containsSpecialCharacter( password );
    }

    private boolean containsSpecialCharacter( String password ) {
        String specialChars = " `!@#$%^&*()_+-=\\]{};':\"|,.<>?~";
        for ( char a : specialChars.toCharArray() ) {
            if ( password.contains( String.valueOf( a ) ) )
                return true;
        }
        return false;
    }
}
