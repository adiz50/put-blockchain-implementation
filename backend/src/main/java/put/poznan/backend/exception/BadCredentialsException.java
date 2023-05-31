package put.poznan.backend.exception;

public class BadCredentialsException extends org.springframework.security.core.AuthenticationException {
    public BadCredentialsException( String msg ) {
        super( msg );
    }
}
