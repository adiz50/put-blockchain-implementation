package put.poznan.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import put.poznan.backend.TestContainersInitializer;
import put.poznan.backend.dto.transaction.request.CreateTransactionRequest;
import put.poznan.backend.dto.transaction.response.CreateTransactionResponse;
import put.poznan.backend.entities.Transaction;
import put.poznan.backend.entities.User;
import put.poznan.backend.repository.TransactionRepository;
import put.poznan.backend.repository.UserRepository;
import put.poznan.backend.testUtils.HeaderProvider;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainersInitializer.class)
@Import(HeaderProvider.class)
class TransactionControllerTest {

    private final TestRestTemplate restTemplate;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final HeaderProvider headerProvider;
    private final List< User > users = new ArrayList<>();

    @Autowired
    public TransactionControllerTest( UserRepository userRepository, TestRestTemplate restTemplate,
                                      PasswordEncoder encoder, TransactionRepository transactionRepository,
                                      HeaderProvider headerProvider ) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.encoder = encoder;
        this.transactionRepository = transactionRepository;
        this.headerProvider = headerProvider;
    }

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        transactionRepository.deleteAll();
        for ( int i = 0 ; i < 2 ; i++ ) {
            User user = User.builder().username( "user" + i ).password( encoder.encode( "test" + i ) ).build();
            User temp = userRepository.save( user );
            user.setId( temp.getId() );
            user.setPassword( "test" + i );
            users.add( user );
        }
    }

    @Test
    public void addTransaction_correct() {
        //Given
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .value( 20.0 )
                .build();
        //When
        ResponseEntity< CreateTransactionResponse > response = restTemplate.exchange( "/api/transaction",
                HttpMethod.POST, new HttpEntity<>( req, headerProvider.getUserHeader( users.get( 0 ) ) ),
                CreateTransactionResponse.class );
        //Then
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        List< Transaction > transactions = transactionRepository.findAll();
        assertEquals( 1, transactions.size() );
    }

    @Test
    public void addTransaction_incorrectValue() {
        //Given
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .value( - 20.0 )
                .build();
        //When
        ResponseEntity< String > response = restTemplate.exchange( "/api/transaction",
                HttpMethod.POST, new HttpEntity<>( req, headerProvider.getUserHeader( users.get( 0 ) ) ),
                String.class );
        //Then
        assertNotEquals( HttpStatus.OK, response.getStatusCode() );
        List< Transaction > transactions = transactionRepository.findAll();
        assertTrue( transactions.isEmpty() );
    }

}