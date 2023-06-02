package put.poznan.backend.dto.transaction.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateTransactionRequest {
    private UUID sender;
    private UUID recipient;
    private Double value;
}
