package put.poznan.backend.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import put.poznan.backend.entities.User;
import put.poznan.backend.exception.BadCredentialsException;
import put.poznan.backend.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {
        User user = userRepository.findByUsername( authentication.getPrincipal().toString() ).orElseThrow(
                () -> new BadCredentialsException( "Bad credentials" )
        );
        if ( ! passwordEncoder.matches( authentication.getCredentials().toString(), user.getPassword() ) )
            throw new BadCredentialsException( "Bad credentials" );
        return authentication;
    }
}
