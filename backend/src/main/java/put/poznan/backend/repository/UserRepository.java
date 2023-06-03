package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository< User, UUID > {

    @Query(value = "select u from User u where u.username = ?1")
    Optional< User > findByUsername( String username );

    @Query(value = "select count(u) > 0 FROM User u WHERE u.username = ?1")
    boolean existsByUsername( String username );
}
