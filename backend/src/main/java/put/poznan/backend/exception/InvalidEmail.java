package put.poznan.backend.exception;

public class InvalidEmail extends RuntimeException {
    public InvalidEmail( String msg ) {
        super( msg );
    }
}

