package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Verify;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerifyRepository extends JpaRepository<Verify, UUID> {
    @Query(value = "select v from Verify v where v.id = ?1 ")
    Optional<Verify> findById( UUID uuid );
}
