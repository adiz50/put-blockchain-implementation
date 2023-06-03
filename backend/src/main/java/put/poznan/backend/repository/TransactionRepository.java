package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Transaction;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository< Transaction, UUID > {

    @Query(value = "select * from transaction where value = (select max(c.value) from transaction as c where " +
            "block_index is null) limit 1",
            nativeQuery = true)
    List< Transaction > getHighestValueTransactions();

    @Query(value = "select * from transaction where block_index is null", nativeQuery = true)
    List< Transaction > getWaitingTransactions();
}
