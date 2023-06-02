package put.poznan.backend.dto.transaction.response;

import lombok.Data;
import put.poznan.backend.entities.Transaction;

import java.util.UUID;

@Data
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
