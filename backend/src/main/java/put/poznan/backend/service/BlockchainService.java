package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import put.poznan.backend.entities.Block;
import put.poznan.backend.exception.BlockInvalid;
import put.poznan.backend.repository.BlockchainRepository;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final BlockchainRepository blockchainRepository;
    @Value("${blockchain.difficulty}")
    private int difficulty;

    public Block addBlock( Block block ) throws NoSuchAlgorithmException {
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

    }
}
