package put.poznan.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import put.poznan.backend.utils.BinaryUtils;
import put.poznan.backend.utils.HashingUtils;

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

    @Builder
    public Block( LocalDateTime timestamp, List< Transaction > data, String previousHash, String hash,
                  String nonce ) {
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.nonce = nonce;
        this.hash = calculateHash();
    }

    private String calculateHash() {
        return HashingUtils.hash( extractValuesToHash() );
    }

    private String extractValuesToHash() {
        return index + timestamp.toString() + data.toString() + previousHash + nonce;
    }

    public boolean isValid( Block prev, int difficulty ) throws NoSuchAlgorithmException {
        if ( ! this.hash.equals( this.calculateHash() ) ) return false;
        if ( ! this.previousHash.equals( prev.getHash() ) ) return false;
        if ( ! HashingUtils.isEqual( hash, extractValuesToHash() ) ) return false;
        if ( BinaryUtils.countLeadingZeros( hash ) < difficulty ) return false;
        return true;
    }

    /**
     * Only used for first block in chain
     */
    public boolean isValid( int difficulty ) throws NoSuchAlgorithmException {
        if ( ! this.hash.equals( this.calculateHash() ) ) return false;
        if ( ! HashingUtils.isEqual( hash, extractValuesToHash() ) ) return false;
        if ( BinaryUtils.countLeadingZeros( hash ) < difficulty ) return false;
        return true;
    }
}
