package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Transaction;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository< Transaction, UUID > {
}
