package recarga.pay.recargatransactions.application.debit.create;

import org.springframework.stereotype.Service;
import recarga.pay.recargatransactions.application.debit.fail.DebitFailedEvent;
import recarga.pay.recargatransactions.application.debit.success.DebitSucceededEvent;
import recarga.pay.recargatransactions.domain.Balance;
import recarga.pay.recargatransactions.domain.debit.Debit;
import recarga.pay.recargatransactions.infrastructure.messaging.ProducerService;
import recarga.pay.recargatransactions.infrastructure.messaging.Topics;
import recarga.pay.recargatransactions.infrastructure.repositories.BalanceRepository;
import recarga.pay.recargatransactions.infrastructure.repositories.DebitRepository;

import java.util.Optional;

@Service
public class CreateDebitUseCase {

    private final DebitRepository debitRepository;
    private final BalanceRepository balanceRepository;
    private final ProducerService producerService;

    public CreateDebitUseCase(DebitRepository debitRepository, BalanceRepository balanceRepository, ProducerService producerService) {
        this.debitRepository = debitRepository;
        this.balanceRepository = balanceRepository;
        this.producerService = producerService;
    }

    public void execute(CreateDebitCommand command) {

        Optional<Debit> existingDebit = debitRepository.findById(command.id());

        if (existingDebit.isPresent()) {
            sendDebitFailedEvent(existingDebit.get(), "DebitConflict", "Debit already exists");
            return;
        }

        var balance = balanceRepository.findBalanceByAccountAgencyAndAccountNumber(command.accountAgency(), command.accountNumber());
        var debit = saveDebit(command);

        if (balance == null){
            debit.fail();
            debitRepository.save(debit);
            sendDebitFailedEvent(debit, "CreditAccount", "Account does not exist");
            return;
        }

        if (balance.getAmount().compareTo(command.amount()) < 0) {
            debit.fail();
            debitRepository.save(debit);
            sendDebitFailedEvent(debit, "Amount", "Insufficient amount for debit transaction");
            return;
        }

        updateBalance(debit, balance);

        debit.success();
        debitRepository.save(debit);

        sendDebitSucceededEvent(debit);
    }

    private void sendDebitSucceededEvent(Debit createdDebit) {
        var debitSucceededEvent = new DebitSucceededEvent(
                createdDebit.getId(),
                createdDebit.getAmount(),
                createdDebit.getAccountNumber(),
                createdDebit.getAccountAgency(),
                createdDebit.getReferenceDate(),
                createdDebit.getDescription());

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, debitSucceededEvent.getKey() , debitSucceededEvent, DebitSucceededEvent.class);
    }

    private void sendDebitFailedEvent(Debit debit, String errorCode, String errorMessage) {
        var key = String.format("%d#%d", debit.getAccountAgency(), debit.getAccountNumber());
        var debitFailedEvent = new DebitFailedEvent(
                debit.getId(),
                debit.getAccountAgency(),
                debit.getAccountNumber(),
                debit.getAmount(),
                errorCode,
                errorMessage);

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key , debitFailedEvent, DebitFailedEvent.class);
    }

    private Debit saveDebit(CreateDebitCommand command) {
        var createdDebit = new Debit(
                command.id(),
                command.amount(),
                command.accountNumber(),
                command.accountAgency(),
                command.referenceDate(),
                command.description()
        );

        debitRepository.save(createdDebit);
        return createdDebit;
    }

    private void updateBalance(Debit debit, Balance balance) {
        var updatedAmount = balance.getAmount().subtract(debit.getAmount());
        balance.setAmount(updatedAmount);
        balanceRepository.save(balance);
    }
}
