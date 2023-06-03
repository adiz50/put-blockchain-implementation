package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import put.poznan.backend.dto.transaction.request.CreateTransactionRequest;
import put.poznan.backend.dto.transaction.response.CreateTransactionResponse;
import put.poznan.backend.entities.Transaction;
import put.poznan.backend.entities.User;
import put.poznan.backend.exception.AccessDenied;
import put.poznan.backend.exception.InvalidTransaction;
import put.poznan.backend.repository.TransactionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public CreateTransactionResponse addTransaction( CreateTransactionRequest req ) {
        Optional< User > loggedIn = userService.currentlyLoggedInUser();
        if ( ! req.getSender().equals( loggedIn.get().getId() ) ) throw new AccessDenied( "Access denied" );
        Transaction transaction = new Transaction( req );
        if ( ! transaction.isValid() ) throw new InvalidTransaction( "Transaction is invalid" );
        transaction = transactionRepository.save( transaction );
        return new CreateTransactionResponse( transaction );
    }

}
