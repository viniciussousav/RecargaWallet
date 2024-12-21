package recarga.pay.recargatransactions.application.transfer.success;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransferSucceededEvent(
        UUID id,
        BigDecimal amount,
        Receiver receiver,
        Payer payer,
        OffsetDateTime referenceDate
) {
    public record Receiver(int agency, int account, String description) { }
    public record Payer(int agency, int account, String description) { }
}