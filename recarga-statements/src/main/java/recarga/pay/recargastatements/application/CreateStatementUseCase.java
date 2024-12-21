package recarga.pay.recargastatements.application;

import org.springframework.stereotype.Service;
import recarga.pay.recargastatements.domain.OperationType;
import recarga.pay.recargastatements.domain.Statement;
import recarga.pay.recargastatements.domain.StatementRepository;

import java.math.BigDecimal;

@Service
public class CreateStatementUseCase {

    private final StatementRepository statementRepository;

    public CreateStatementUseCase(final StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public void execute(CreateStatementCommand command) {

        if (statementRepository.existsByTransactionIdAndAccountAgencyAndAccountNumber(command.id(), command.agency(), command.account()))
            return;

        var lastStatement = statementRepository.findTopByAccountAgencyAndAccountNumberOrderByReferenceDateDesc(command.agency(), command.account());

        if (lastStatement == null) {
            var newStatement = createNewStatement(command, BigDecimal.ZERO);
            statementRepository.save(newStatement);
            return;
        }

        var newStatement = createNewStatement(command, lastStatement.getBalance());
        statementRepository.save(newStatement);
    }

    public void execute(CreateStatementCommand creditStatementCommand, CreateStatementCommand debitStatementCommand) {
        execute(creditStatementCommand);
        execute(debitStatementCommand);
    }

    private static Statement createNewStatement(CreateStatementCommand command, BigDecimal previousBalance) {
        var currentBalance = command.operationType() == OperationType.CREDIT
                ? previousBalance.add(command.amount())
                : previousBalance.subtract(command.amount());

        var currentAmount = command.operationType() == OperationType.DEBIT
                ? command.amount().negate()
                : command.amount();

        return new Statement(command.id(), currentAmount, command.account(), command.agency(),
                command.referenceDate(), command.description(), command.operationType(), currentBalance);
    }

}
