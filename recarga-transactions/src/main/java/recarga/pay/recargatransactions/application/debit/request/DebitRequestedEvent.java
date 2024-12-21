package recarga.pay.recargatransactions.application.debit.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DebitRequestedEvent(
        UUID id,
        BigDecimal amount,
        int accountNumber,
        int accountAgency,
        OffsetDateTime referenceDate,
        String description) {
}