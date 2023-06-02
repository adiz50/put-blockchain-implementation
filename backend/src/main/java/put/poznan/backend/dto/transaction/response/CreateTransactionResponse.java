package put.poznan.backend.dto.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.poznan.backend.entities.Transaction;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionResponse {
    private UUID id;
    private UUID sender;
    private UUID recipient;
    private Double value;

    public CreateTransactionResponse( Transaction req ) {
        this.id = req.getId();
        this.sender = req.getSender();
        this.recipient = req.getRecipient();
        this.value = req.getValue();
    }
}
