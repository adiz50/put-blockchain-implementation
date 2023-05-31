package put.poznan.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class UsernameTakenException extends AuthenticationException {
    public UsernameTakenException( String msg ) {
        super( msg );
    }
}
