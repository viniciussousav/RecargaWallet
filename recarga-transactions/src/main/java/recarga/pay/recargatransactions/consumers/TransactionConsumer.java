package recarga.pay.recargatransactions.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import recarga.pay.recargatransactions.application.credit.create.CreateCreditCommand;
import recarga.pay.recargatransactions.application.credit.create.CreateCreditUseCase;
import recarga.pay.recargatransactions.application.credit.request.CreditRequestedEvent;
import recarga.pay.recargatransactions.application.debit.create.CreateDebitCommand;
import recarga.pay.recargatransactions.application.debit.create.CreateDebitUseCase;
import recarga.pay.recargatransactions.application.debit.request.DebitRequestedEvent;
import recarga.pay.recargatransactions.application.transfer.create.CreateTransferCommand;
import recarga.pay.recargatransactions.application.transfer.create.CreateTransferUseCase;
import recarga.pay.recargatransactions.application.transfer.request.TransferRequestedEvent;
import recarga.pay.recargatransactions.infrastructure.messaging.GroupIds;
import recarga.pay.recargatransactions.infrastructure.messaging.Topics;

import java.util.Optional;

@Component
public class TransactionConsumer {

        private final ObjectMapper objectMapper;

    private final CreateCreditUseCase createCreditUseCase;

    private final CreateDebitUseCase createDebitUseCase;

    private final CreateTransferUseCase createTransferUseCase;

    public TransactionConsumer(
            ObjectMapper objectMapper,
            CreateCreditUseCase createCreditUseCase,
            CreateDebitUseCase createDebitUseCase,
            CreateTransferUseCase createTransferUseCase) {
        this.objectMapper = objectMapper;

        this.createCreditUseCase = createCreditUseCase;
        this.createDebitUseCase = createDebitUseCase;
        this.createTransferUseCase = createTransferUseCase;
    }

    @KafkaListener(topics = {Topics.RECARGA_TRANSACTIONS} , groupId = GroupIds.RECARGA_TRANSACTIONS)
    public void consume(ConsumerRecord<String, String> record) {

        var eventType = getEventType(record);

        try {
            switch (eventType) {
                case "CreditRequestedEvent":
                    var creditRequestedEvent = this.objectMapper.readValue(record.value(), CreditRequestedEvent.class);
                    handleCreditRequestedEvent(creditRequestedEvent);
                    break;
                case "DebitRequestedEvent":
                    var debitRequestedEvent = this.objectMapper.readValue(record.value(), DebitRequestedEvent.class);
                    handleDebitRequestedEvent(debitRequestedEvent);
                    break;
                case "TransferRequestedEvent":
                    var transferRequestedEvent = this.objectMapper.readValue(record.value(), TransferRequestedEvent.class);
                    handleTransferRequestedEvent(transferRequestedEvent);
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

    public void handleCreditRequestedEvent(CreditRequestedEvent event) {
        var command = new CreateCreditCommand(
                event.id(),
                event.amount(),
                event.accountNumber(),
                event.accountAgency(),
                event.referenceDate(),
                event.description());

        createCreditUseCase.execute(command);
    }

    public void handleDebitRequestedEvent(DebitRequestedEvent event) {
        var command = new CreateDebitCommand(
                event.id(),
                event.amount(),
                event.accountNumber(),
                event.accountAgency(),
                event.referenceDate(),
                event.description());

        createDebitUseCase.execute(command);
    }

    public void handleTransferRequestedEvent(TransferRequestedEvent event) {
        var command = new CreateTransferCommand(
                event.id(),
                event.amount(),
                new CreateTransferCommand.Receiver(
                        event.receiver().agency(),
                        event.receiver().account(),
                        event.receiver().description()),
                new CreateTransferCommand.Payer(
                        event.payer().agency(),
                        event.payer().account(),
                        event.payer().description()),
                event.referenceDate());

        createTransferUseCase.execute(command);
    }
}
