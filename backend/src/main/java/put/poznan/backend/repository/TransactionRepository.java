package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository< Transaction, UUID > {

    @Query(value = "select t from Transaction t where t.value = max(t.value)")
    List< Transaction > getHighestValueTransactions();
}
