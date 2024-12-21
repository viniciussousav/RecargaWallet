package recarga.pay.recargatransactions.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recarga.pay.recargatransactions.application.credit.request.CreateCreditRequest;
import recarga.pay.recargatransactions.application.credit.request.CreditRequestedEvent;
import recarga.pay.recargatransactions.application.debit.request.CreateDebitRequest;
import recarga.pay.recargatransactions.application.debit.request.DebitRequestedEvent;
import recarga.pay.recargatransactions.application.transfer.request.CreateTransferRequest;
import recarga.pay.recargatransactions.application.transfer.request.TransferRequestedEvent;
import recarga.pay.recargatransactions.infrastructure.messaging.ProducerService;
import recarga.pay.recargatransactions.infrastructure.messaging.Topics;

@RestController
@Validated
public class TransactionsController {

    private final ProducerService producerService;

    public TransactionsController(
            ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/credit")
    public ResponseEntity<Object> CreateCredit(
            @Valid @RequestBody CreateCreditRequest body) {
        var key = String.format("%d#%d", body.accountAgency(), body.accountNumber());
        var creditRequestedEvent = new CreditRequestedEvent(
                body.id(), body.amount(), body.accountNumber(), body.accountAgency(), body.referenceDate(), body.description());

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key, creditRequestedEvent, CreditRequestedEvent.class);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/debit")
    public ResponseEntity<Object> CreateDebit(
            @Valid @RequestBody CreateDebitRequest body) {

        var key = String.format("%d#%d", body.accountAgency(), body.accountNumber());
        var debitRequestedEvent = new DebitRequestedEvent(
                body.id(), body.amount(), body.accountNumber(), body.accountAgency(), body.referenceDate(), body.description());

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key, debitRequestedEvent, DebitRequestedEvent.class);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> CreateTransfer(
            @Valid @RequestBody CreateTransferRequest body) {

        var key = String.format("%d#%d", body.payer().agency(), body.payer().account());
        var transferRequestedEvent = new TransferRequestedEvent(
                body.id(),
                body.amount(),
                new TransferRequestedEvent.Receiver(body.receiver().agency(), body.receiver().account(), body.receiver().description()),
                new TransferRequestedEvent.Payer(body.payer().agency(), body.payer().account(), body.payer().description()),
                body.referenceDate());

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key, transferRequestedEvent, TransferRequestedEvent.class);
        return ResponseEntity.accepted().build();
    }
}
