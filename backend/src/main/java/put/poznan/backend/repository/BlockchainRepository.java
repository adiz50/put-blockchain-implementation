package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Block;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockchainRepository extends JpaRepository< Block, Long > {

    @Query(value = "select c from Block c where c.index = (select max(b.index) from Block b)")
    Optional< Block > getLastBlock();

    @Query(value = "select t.block from Transaction t where t.id =?1")
    Optional< Block > getBlockByTransactionId( UUID transactionId );
}
