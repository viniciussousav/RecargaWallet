package recarga.pay.recargatransactions.application.credit.create;

import org.springframework.stereotype.Service;
import recarga.pay.recargatransactions.application.credit.fail.CreditFailedEvent;
import recarga.pay.recargatransactions.application.credit.success.CreditSucceededEvent;
import recarga.pay.recargatransactions.domain.Balance;
import recarga.pay.recargatransactions.domain.credit.Credit;
import recarga.pay.recargatransactions.infrastructure.messaging.ProducerService;
import recarga.pay.recargatransactions.infrastructure.messaging.Topics;
import recarga.pay.recargatransactions.infrastructure.repositories.BalanceRepository;
import recarga.pay.recargatransactions.infrastructure.repositories.CreditRepository;

import java.util.Optional;

@Service
public class CreateCreditUseCase {

    private final CreditRepository creditRepository;
    private final BalanceRepository balanceRepository;
    private final ProducerService producerService;

    public CreateCreditUseCase(CreditRepository creditRepository,  BalanceRepository balanceRepository, ProducerService producerService) {
        this.creditRepository = creditRepository;
        this.balanceRepository = balanceRepository;
        this.producerService = producerService;
    }

    public void execute(CreateCreditCommand command) {

        Optional<Credit> existingCredit = creditRepository.findById(command.id());

        if (existingCredit.isPresent()) {
            produceCreditFailedEvent(command, "Conflict","Already exists a credit with same id");
            return;
        }

        var balance = balanceRepository.findBalanceByAccountAgencyAndAccountNumber(command.accountAgency(), command.accountNumber());

        var credit = createCredit(command);

        if (balance == null) {
            credit.fail();
            creditRepository.save(credit);
            produceCreditFailedEvent(command, "CreditAccount", "Account does not exists");
            return;
        }

        updateBalance(credit, balance);

        credit.success();
        creditRepository.save(credit);

        produceCreditSucceededEvent(credit);
    }

    private void produceCreditSucceededEvent(Credit credit) {
        var creditSucceededEvent = new CreditSucceededEvent(
                credit.getId(),
                credit.getAmount(),
                credit.getAccountNumber(),
                credit.getAccountAgency(),
                credit.getReferenceDate(),
                credit.getDescription());

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, creditSucceededEvent.getKey() , creditSucceededEvent, CreditSucceededEvent.class);
    }

    private void produceCreditFailedEvent(CreateCreditCommand command, String errorCode, String errorMessage) {
        var key = String.format("%d#%d", command.accountAgency(), command.accountNumber());

        var creditFailedEvent = new CreditFailedEvent(
                command.id(), command.accountAgency(), command.accountNumber(), command.amount(), errorCode, errorMessage);

        producerService.sendMessage(Topics.RECARGA_TRANSACTIONS, key , creditFailedEvent, CreditFailedEvent.class);
    }

    private Credit createCredit(CreateCreditCommand command) {
        var newCredit = new Credit(
                command.id(),
                command.amount(),
                command.accountNumber(),
                command.accountAgency(),
                command.referenceDate(),
                command.description());

        creditRepository.save(newCredit);
        return newCredit;
    }

    private void updateBalance(Credit credit, Balance balance) {
        var updatedAmount = balance.getAmount().add(credit.getAmount());
        balance.setAmount(updatedAmount);
        balanceRepository.save(balance);
    }
}
