package put.poznan.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import put.poznan.backend.dto.blockchain.TransactionData;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "block")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long index;
    private LocalDateTime timestamp;
    @OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List< Transaction > data;
    private String previousHash;
    private String hash;
    private String nonce;

    public String calculateHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
        String toHash = extractValuesToHash();
        byte[] hash = digest.digest( toHash.getBytes( StandardCharsets.UTF_8 ) );
        return new String( hash, StandardCharsets.UTF_8 );
    }

    private String extractValuesToHash() {
        return index + timestamp.toString() + data.toString() + previousHash + nonce;
    }

    //TODO implement
    public boolean isValid( Block prev ) throws NoSuchAlgorithmException {

        return true;
    }
}
