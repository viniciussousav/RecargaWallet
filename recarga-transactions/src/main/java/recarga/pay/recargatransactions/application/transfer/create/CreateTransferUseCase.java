package recarga.pay.recargatransactions.application.transfer.create;

import org.springframework.stereotype.Service;
import recarga.pay.recargatransactions.application.transfer.fail.TransferFailedEvent;
import recarga.pay.recargatransactions.application.transfer.success.TransferSucceededEvent;
import recarga.pay.recargatransactions.domain.Balance;
import recarga.pay.recargatransactions.domain.credit.Credit;
import recarga.pay.recargatransactions.domain.debit.Debit;
import recarga.pay.recargatransactions.domain.transfer.Transfer;
import recarga.pay.recargatransactions.infrastructure.messaging.ProducerService;
import recarga.pay.recargatransactions.infrastructure.messaging.Topics;
import recarga.pay.recargatransactions.infrastructure.repositories.BalanceRepository;
import recarga.pay.recargatransactions.infrastructure.repositories.TransferRepository;

import java.util.UUID;

@Service
public class CreateTransferUseCase {

    private final TransferRepository transferRepository;
    private final BalanceRepository balanceRepository;

    private final ProducerService producerService;

    public CreateTransferUseCase(
            TransferRepository transferRepository,
            BalanceRepository balanceRepository,
            ProducerService producerService) {
        this.transferRepository = transferRepository;
        this.balanceRepository = balanceRepository;
        this.producerService = producerService;
    }

    public void execute(CreateTransferCommand command) {

        var existingTransfer = transferRepository.findById(command.id());
        if (existingTransfer.isPresent()){
            sendTransferFailedEvent(existingTransfer.get(), "Conflict", "Already exist a transfer with same id");
            return;
        }

        var transfer = CreateTransfer(command);

        var payerBalance = balanceRepository.findBalanceByAccountAgencyAndAccountNumber(command.payer().agency(), command.payer().account());

        if (payerBalance == null) {
            transfer.fail();
            transferRepository.save(transfer);
            sendTransferFailedEvent(transfer, "PayerAccount", "Payer account does not exist");
            return;
        }

        if (payerBalance.getAmount().compareTo(command.amount()) < 0) {
            transfer.fail();
            transferRepository.save(transfer);
            sendTransferFailedEvent(transfer, "PayerAmount", "Insufficient amount");
            return;
        }

        var receiverBalance = balanceRepository.findBalanceByAccountAgencyAndAccountNumber(command.receiver().agency(), command.receiver().account());
        if (receiverBalance == null) {
            transfer.fail();
            transferRepository.save(transfer);
            sendTransferFailedEvent(transfer, "ReceiverAccount", "Receiver account does not exist");
            return;
        }

        debitPayer(payerBalance, transfer);
        creditReceiver(receiverBalance, transfer);

        transfer.success();
        transferRepository.save(transfer);

        sendTransferSucceededEvent(transfer);
    }

    public void debitPayer(Balance payerBalance, Transfer transfer) {
        var updatedAmount = payerBalance.getAmount().subtract(transfer.getAmount());
        payerBalance.setAmount(updatedAmount);
        balanceRepository.save(payerBalance);
    }

    public void creditReceiver(Balance receiverBalance, Transfer transfer) {
        var updatedAmount = receiverBalance.getAmount().add(transfer.getAmount());
        receiverBalance.setAmount(updatedAmount);
        balanceRepository.save(receiverBalance);
    }

    public Transfer CreateTransfer(CreateTransferCommand command) {
        var transfer = new Transfer(
            command.id(),
            command.amount(),
            new Credit(
                UUID.randomUUID(),
                command.amount(),
                command.receiver().account(),
                command.receiver().agency(),
                command.referenceDate(),
                command.receiver().description()),
            new Debit(
                UUID.randomUUID(),
                command.amount(),
                command.payer().account(),
                command.payer().agency(),
                command.referenceDate(),
                command.payer().description()),
            command.referenceDate());

        transferRepository.save(transfer);
        return transfer;
    }

    public void sendTransferFailedEvent(Transfer transfer, String errorCode, String errorMessage) {
        var key = String.format("%d#%d", transfer.getDebit().getAccountAgency(), transfer.getDebit().getAccountNumber());
        var transferFailedEvent = new TransferFailedEvent(transfer.getId(), errorCode, errorMessage);

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key , transferFailedEvent, TransferFailedEvent.class);
    }

    public void sendTransferSucceededEvent(Transfer transfer) {
        var key = String.format("%d#%d", transfer.getDebit().getAccountAgency(), transfer.getDebit().getAccountNumber());

        var transferSucceededEvent = new TransferSucceededEvent(
            transfer.getId(),
            transfer.getAmount(),
            new TransferSucceededEvent.Receiver(
                transfer.getCredit().getAccountAgency(),
                transfer.getCredit().getAccountNumber(),
                transfer.getCredit().getDescription()),
            new TransferSucceededEvent.Payer(
                transfer.getDebit().getAccountAgency(),
                transfer.getDebit().getAccountNumber(),
                transfer.getDebit().getDescription()),
            transfer.getReferenceDate());

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key , transferSucceededEvent, TransferSucceededEvent.class);
    }
}
