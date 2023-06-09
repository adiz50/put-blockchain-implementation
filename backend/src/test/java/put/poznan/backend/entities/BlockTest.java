package put.poznan.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import put.poznan.backend.TestContainersInitializer;
import put.poznan.backend.repository.TransactionRepository;
import put.poznan.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainersInitializer.class)
public class BlockTest {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder encoder;
    private final List< User > users = new ArrayList<>();

    @Autowired
    public BlockTest( UserRepository userRepository, TransactionRepository transactionRepository,
                      PasswordEncoder encoder ) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.encoder = encoder;
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
    public void isValid_correctFirstBlock_ignoreDifficulty() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        assertTrue( block.isValid( 0 ) );
    }

    @Test
    public void isValid_correctNextBlock_ignoreDifficulty() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block1 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        transactions.clear();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 15.50 )
                        .build()
        );
        Block block2 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( block1.getHash() )
                .build();
        assertTrue( block2.isValid( block1, 0 ) );
    }

    @Test
    public void shouldNotReturnTransactionInBlock() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block1 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        transactions.clear();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 15.50 )
                        .build()
        );
        transactionRepository.save( Transaction.builder()
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .value( 16.50 )
                .build() );
        assertEquals( 1, transactionRepository.getHighestValueTransactions().size() );
    }

    @Test
    public void shouldNotReturnAnyTransaction() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block1 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        transactions.clear();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 15.50 )
                        .build()
        );
        assertEquals( 0, transactionRepository.getHighestValueTransactions().size() );
    }

    @Test
    public void isValid_firstBlockTooLowDifficulty() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        assertFalse( block.isValid( 300 ) );
    }

    @Test
    public void isValid_nextBlockTooLowDifficulty() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block1 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        transactions.clear();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 15.50 )
                        .build()
        );
        Block block2 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( block1.getHash() )
                .build();
        assertFalse( block2.isValid( block1, 300 ) );
    }

    @Test
    public void isValid_nextBlockWrongPrevHash() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block1 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        transactions.clear();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 15.50 )
                        .build()
        );
        Block block2 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .build();
        block2.setPreviousHash( block2.getHash() );
        assertFalse( block2.isValid( block1, 0 ) );
    }

    @Test
    public void isValid_nextBlockWrongHash() {
        List< Transaction > transactions = new ArrayList<>();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 12.50 )
                        .build()
        );
        Block block1 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( "0" )
                .build();
        transactions.clear();
        transactions.add(
                Transaction.builder()
                        .sender( users.get( 0 ).getId() )
                        .recipient( users.get( 1 ).getId() )
                        .value( 15.50 )
                        .build()
        );
        Block block2 = Block.builder()
                .data( transactions )
                .nonce( "1" )
                .timestamp( LocalDateTime.now() )
                .previousHash( block1.getHash() )
                .build();
        block2.setHash( block1.getHash() );
        assertFalse( block2.isValid( block1, 0 ) );
    }
}