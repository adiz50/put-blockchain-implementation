package put.poznan.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import put.poznan.backend.TestContainersInitializer;
import put.poznan.backend.dto.transaction.request.CreateTransactionRequest;
import put.poznan.backend.entities.Block;
import put.poznan.backend.entities.Transaction;
import put.poznan.backend.entities.User;
import put.poznan.backend.repository.BlockchainRepository;
import put.poznan.backend.repository.TransactionRepository;
import put.poznan.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainersInitializer.class)
class BlockchainServiceTest {

    private final UserRepository userRepository;
    private final BlockchainRepository blockchainRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final List< User > users = new ArrayList<>();

    @Autowired
    public BlockchainServiceTest( UserRepository userRepository, BlockchainRepository blockchainRepository,
                                  TransactionService transactionService, TransactionRepository transactionRepository ) {
        this.userRepository = userRepository;
        this.blockchainRepository = blockchainRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        blockchainRepository.deleteAll();
        transactionRepository.deleteAll();
        for ( int i = 0 ; i < 2 ; i++ ) {
            User user = User.builder().username( "user" + i ).password( "test" + i ).build();
            User temp = userRepository.save( user );
            user.setId( temp.getId() );
            users.add( user );
        }
    }

    @Test
    void mineBlock_mineBlockInUnder1Minute() {
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .value( 20.0 )
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .build();
        transactionService.addTransaction( req );
        Optional< Block > block = blockchainRepository.getLastBlock();
        long startSec = System.currentTimeMillis() / 1000;
        while ( System.currentTimeMillis() / 1000 - startSec < 60 ) {
            block = blockchainRepository.getLastBlock();
            if ( block.isPresent() ) break;
        }
        assertTrue( block.isPresent() );
    }

    @Test
    void mineBlock_mineFor1MinuteAndCheck() throws InterruptedException {
        //Given
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .value( 20.0 )
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .build();
        transactionService.addTransaction( req );
        req = CreateTransactionRequest.builder()
                .value( 21.0 )
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .build();
        transactionService.addTransaction( req );
        req = CreateTransactionRequest.builder()
                .value( 22.0 )
                .sender( users.get( 0 ).getId() )
                .recipient( users.get( 1 ).getId() )
                .build();
        transactionService.addTransaction( req );
        //When
        Thread.sleep( 60000 );
        //Then
        List< Block > blocks = blockchainRepository.findAll();
        assertEquals( 3, blocks.size() );
        List< Transaction > transactions = transactionRepository.findAll();
        assertEquals( 3, transactions.size() );
    }
}