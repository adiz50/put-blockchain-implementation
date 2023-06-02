package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
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

    //TODO finish it
    public Block addBlock( Block block ) throws NoSuchAlgorithmException {
        Optional< Block > prevBlock = blockchainRepository.getLastBlock();
        if ( prevBlock.isPresent() ) {
            if ( ! block.isValid( prevBlock.get() ) ) throw new BlockInvalid( "Block is invalid" );

        } else {

        }

        return blockchainRepository.save( block );
    }

    //TODO implement
    @Scheduled(fixedDelay = 10000)
    public void mineBlock() {

    }
}
