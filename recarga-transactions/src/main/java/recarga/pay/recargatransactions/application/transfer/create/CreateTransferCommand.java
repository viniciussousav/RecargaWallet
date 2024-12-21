package recarga.pay.recargatransactions.application.transfer.create;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTransferCommand (
    UUID id,
    BigDecimal amount,
    Receiver receiver,
    Payer payer,
    OffsetDateTime referenceDate
) {
    public record Receiver(int agency, int account, String description) { }
    public record Payer(int agency, int account, String description) { }
}