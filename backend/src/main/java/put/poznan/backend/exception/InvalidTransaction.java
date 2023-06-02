package put.poznan.backend.exception;

public class InvalidTransaction extends RuntimeException {
    public InvalidTransaction( String msg ) {
        super( msg );
    }
}
