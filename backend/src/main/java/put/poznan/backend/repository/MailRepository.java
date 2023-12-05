package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Mail;

import java.util.List;
import java.util.UUID;

@Repository
public interface MailRepository extends JpaRepository<Mail, UUID> {

    @Query(value = "select m from Mail m order by m.id asc limit 10")
    List<Mail> getMailsToSend();
}
