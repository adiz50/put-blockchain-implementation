package put.poznan.backend.exception;

public class AccessDenied extends RuntimeException {
    public AccessDenied( String msg ) {
        super( msg );
    }
}
