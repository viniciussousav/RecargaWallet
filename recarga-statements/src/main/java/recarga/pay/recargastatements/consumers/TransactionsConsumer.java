package recarga.pay.recargastatements.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import recarga.pay.recargastatements.application.CreateStatementCommand;
import recarga.pay.recargastatements.application.CreateStatementUseCase;
import recarga.pay.recargastatements.domain.CreditSucceededEvent;
import recarga.pay.recargastatements.domain.DebitSucceededEvent;
import recarga.pay.recargastatements.domain.OperationType;
import recarga.pay.recargastatements.domain.TransferSucceededEvent;
import recarga.pay.recargastatements.infrastructure.messaging.GroupIds;
import recarga.pay.recargastatements.infrastructure.messaging.Topics;

import java.util.Optional;

@Component
public class TransactionsConsumer {

    private final ObjectMapper objectMapper;
    private final CreateStatementUseCase createStatementUseCase;

    public TransactionsConsumer(ObjectMapper objectMapper, CreateStatementUseCase createStatementUseCase) {
        this.objectMapper = objectMapper;
        this.createStatementUseCase = createStatementUseCase;
    }

    @KafkaListener(topics = {Topics.RECARGA_TRANSACTIONS}, groupId = GroupIds.RECARGA_STATEMENTS)
    public void consume(final ConsumerRecord<String, String> record) {
        var eventType = getEventType(record);

        try {
            switch (eventType) {
                case "CreditSucceededEvent":
                    var creditSucceededEvent = this.objectMapper.readValue(record.value(), CreditSucceededEvent.class);
                    handleCreditSucceededEvent(creditSucceededEvent);
                    break;
                case "DebitSucceededEvent":
                    var debitSucceededEvent = this.objectMapper.readValue(record.value(), DebitSucceededEvent.class);
                    handleDebitSucceededEvent(debitSucceededEvent);
                    break;
                case "TransferSucceededEvent":
                    var transferSucceededEvent = this.objectMapper.readValue(record.value(), TransferSucceededEvent.class);
                    handleTransferSucceededEvent(transferSucceededEvent);
                    break;
                default:
                    System.err.println("Ignoring event type: " + eventType);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Exception consuming message: " + e.getMessage());
        }
    }

    private static String getEventType(ConsumerRecord<String, String> record) {
        var rawEventType = Optional.ofNullable(record.headers().lastHeader("EventType"));

        return rawEventType.map(header -> new String(header.value())).orElse("");

    }

    private void handleCreditSucceededEvent(CreditSucceededEvent creditSucceededEvent) {
        var command = new CreateStatementCommand(
                creditSucceededEvent.id(),
                creditSucceededEvent.amount(),
                creditSucceededEvent.accountAgency(),
                creditSucceededEvent.accountNumber(),
                creditSucceededEvent.description(),
                OperationType.CREDIT,
                creditSucceededEvent.referenceDate());

        createStatementUseCase.execute(command);
    }

    private void handleDebitSucceededEvent(DebitSucceededEvent debitSucceededEvent) {
        var command = new CreateStatementCommand(
                debitSucceededEvent.id(),
                debitSucceededEvent.amount(),
                debitSucceededEvent.accountAgency(),
                debitSucceededEvent.accountNumber(),
                debitSucceededEvent.description(),
                OperationType.DEBIT,
                debitSucceededEvent.referenceDate());

        createStatementUseCase.execute(command);
    }

    private void handleTransferSucceededEvent(TransferSucceededEvent transferSucceededEvent) {
        var debitTransferStatement = new CreateStatementCommand(
                transferSucceededEvent.id(),
                transferSucceededEvent.amount(),
                transferSucceededEvent.payer().agency(),
                transferSucceededEvent.payer().account(),
                transferSucceededEvent.payer().description(),
                OperationType.DEBIT,
                transferSucceededEvent.referenceDate());

        var creditTransferStatement = new CreateStatementCommand(
                transferSucceededEvent.id(),
                transferSucceededEvent.amount(),
                transferSucceededEvent.receiver().agency(),
                transferSucceededEvent.receiver().account(),
                transferSucceededEvent.receiver().description(),
                OperationType.CREDIT,
                transferSucceededEvent.referenceDate());

        createStatementUseCase.execute(creditTransferStatement, debitTransferStatement);
    }
}
