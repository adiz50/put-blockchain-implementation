package put.poznan.backend.types;

public enum ActionType {
    VERIFY_EMAIL( "VERIFY_EMAIL" ),
    FORGOTTEN_PASSWORD( "FORGOTTEN_PASSWORD" );

    public final String label;

    ActionType( String label ) {
        this.label = label;
    }

    public static ActionType getType( String type ) {
        if ( type.equals( VERIFY_EMAIL.label ) ) {
            return VERIFY_EMAIL;
        } else if ( type.equals( FORGOTTEN_PASSWORD.label ) ) {
            return FORGOTTEN_PASSWORD;
        } else throw new RuntimeException( "Unrecognized question type" );
    }
}
