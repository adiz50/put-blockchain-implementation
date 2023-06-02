package put.poznan.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import put.poznan.backend.TestContainersInitializer;
import put.poznan.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainersInitializer.class)
public class TransactionTest {

    private final UserRepository userRepository;
    private final List< User > users = new ArrayList<>();

    @Autowired
    public TransactionTest( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        for ( int i = 0 ; i < 2 ; i++ ) {
            User user = User.builder().username( "user" + i ).password( "test" + i ).build();
            User temp = userRepository.save( user );
            user.setId( temp.getId() );
            users.add( user );
        }
    }

    @Test
    void isValid_correctTransaction() {
        Transaction transaction = Transaction.builder()
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .value( 12.4 )
                .build();
        assertTrue( transaction.isValid() );
    }

    @Test
    void isValid_negativeValue() {
        Transaction transaction = Transaction.builder()
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .value( - 12.4 )
                .build();
        assertFalse( transaction.isValid() );
    }

    @Test
    void isValid_zeroValue() {
        Transaction transaction = Transaction.builder()
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .value( 0.0 )
                .build();
        assertFalse( transaction.isValid() );
    }
}