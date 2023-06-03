package put.poznan.backend.exception;

public class NotLoggedIn extends RuntimeException {
    public NotLoggedIn( String msg ) {
        super( msg );
    }
}
