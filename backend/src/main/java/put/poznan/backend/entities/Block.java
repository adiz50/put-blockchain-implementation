package put.poznan.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import put.poznan.backend.dto.blockchain.TransactionData;
import put.poznan.backend.exception.BlockInvalid;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "block")
@ToString
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long index;
    @Nonnull
    private LocalDateTime timestamp;
    @OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List< Transaction > data;
    @Nonnull
    private String previousHash;
    @Nonnull
    private String nonce;
    @Nonnull
    private String hash;
    @Value("${blockchain.difficulty}")
    @Transient
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private int difficulty;

    @Builder
    public Block( Long index, LocalDateTime timestamp, List< Transaction > data, String previousHash, String hash,
                  String nonce ) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.nonce = nonce;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance( "SHA-256" );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
            throw new BlockInvalid( "Hashing algorithm not found" );
        }
        String toHash = extractValuesToHash();
        byte[] hash = digest.digest( toHash.getBytes( StandardCharsets.UTF_8 ) );
        return new String( hash, StandardCharsets.UTF_8 );
    }

    private String extractValuesToHash() {
        return index + timestamp.toString() + data.toString() + previousHash + nonce;
    }

    public boolean isValid( Block prev ) throws NoSuchAlgorithmException {
        if ( ! this.hash.equals( this.calculateHash() ) ) return false;
        if ( ! this.previousHash.equals( prev.getHash() ) ) return false;
        return true;
    }

    /**
     * Only used for first block in chain
     */
    public boolean isValid() throws NoSuchAlgorithmException {
        if ( ! this.hash.equals( this.calculateHash() ) ) return false;
        return true;
    }
}
