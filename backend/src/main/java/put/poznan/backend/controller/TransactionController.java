package put.poznan.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.poznan.backend.dto.transaction.request.CreateTransactionRequest;
import put.poznan.backend.dto.transaction.response.CreateTransactionResponse;
import put.poznan.backend.entities.Transaction;
import put.poznan.backend.service.TransactionService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity< CreateTransactionResponse > addTransaction( @RequestBody CreateTransactionRequest req ) {
        return ResponseEntity.ok( service.addTransaction( req ) );
    }

    @GetMapping
    public ResponseEntity< List< Transaction > > getWaitingTransactions() {
        return ResponseEntity.ok( service.getTransactions() );
    }

}
