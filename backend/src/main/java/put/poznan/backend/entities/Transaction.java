package put.poznan.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import put.poznan.backend.dto.transaction.request.CreateTransactionRequest;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ToString.Exclude
    private UUID id;
    private UUID sender;
    private UUID recipient;
    private Double value;
    @ManyToOne(targetEntity = Block.class)
    @JoinColumn(name = "block_index")
    @ToString.Exclude
    private Block block;

    @Builder
    public Transaction( UUID sender, UUID recipient, Double value ) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
    }

    public Transaction( CreateTransactionRequest req ) {
        this.sender = req.getSender();
        this.recipient = req.getRecipient();
        this.value = req.getValue();
    }

    public boolean isValid() {
        return value > 0;
    }
}
