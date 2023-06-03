package put.poznan.backend.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import put.poznan.backend.utils.BinaryUtils;
import put.poznan.backend.utils.HashingUtils;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long index;
    @Nonnull
    private LocalDateTime timestamp;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "block_index")
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
        calculateHash();
    }

    public void calculateHash() {
        this.hash = HashingUtils.hash( extractValuesToHash() );
    }

    private String calculateHashInner() {
        return HashingUtils.hash( extractValuesToHash() );
    }

    private String extractValuesToHash() {
        return index + timestamp.toString() + data.toString() + previousHash + nonce;
    }

    public boolean isValid( Block prev, int difficulty ) {
        if ( ! this.hash.equals( this.calculateHashInner() ) ) return false;
        if ( ! this.previousHash.equals( prev.getHash() ) ) return false;
        if ( ! HashingUtils.isEqual( hash, extractValuesToHash() ) ) return false;
        return BinaryUtils.countLeadingZeros( hash ) >= difficulty;
    }

    /**
     * Only used for first block in chain
     */
    public boolean isValid( int difficulty ) {
        if ( ! this.hash.equals( this.calculateHashInner() ) ) return false;
        if ( ! HashingUtils.isEqual( hash, extractValuesToHash() ) ) return false;
        return BinaryUtils.countLeadingZeros( hash ) >= difficulty;
    }
}
