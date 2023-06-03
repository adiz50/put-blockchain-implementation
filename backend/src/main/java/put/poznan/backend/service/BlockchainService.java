package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import put.poznan.backend.entities.Block;
import put.poznan.backend.entities.Transaction;
import put.poznan.backend.exception.BlockInvalid;
import put.poznan.backend.repository.BlockchainRepository;
import put.poznan.backend.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final BlockchainRepository blockchainRepository;
    private final TransactionRepository transactionRepository;
    @Value("${blockchain.difficulty}")
    private int difficulty;

    public Block addBlock( Block block ) {
        Optional< Block > prevBlock = blockchainRepository.getLastBlock();
        //For every non-first block
        if ( prevBlock.isPresent() && ! block.isValid( prevBlock.get(), difficulty ) )
            throw new BlockInvalid( "Block is invalid" );
        //For first block
        if ( prevBlock.isEmpty() && ! block.isValid( difficulty ) )
            throw new BlockInvalid( "Block is invalid" );
        return blockchainRepository.save( block );
    }

    //TODO implement
    @Scheduled(fixedDelay = 10000)
    public void mineBlock() {
        Optional< Block > last = blockchainRepository.getLastBlock();
        List< Transaction > transactions = transactionRepository.getHighestValueTransactions();
        if ( transactions.isEmpty() ) return;
        Block newBlock = Block.builder()
                .timestamp( LocalDateTime.now() )
                .data( List.of( transactions.get( 0 ) ) )
                .build();
        if ( last.isPresent() ) newBlock.setPreviousHash( last.get().getHash() );
        else newBlock.setPreviousHash( "0" );
        int i = 0;
        while ( ! newBlock.isValid( difficulty ) ) {
            newBlock.setNonce( String.valueOf( i ) );
            newBlock.calculateHash();
            i++;
        }
        addBlock( newBlock );
    }
}
