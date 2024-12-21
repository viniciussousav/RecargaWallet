package recarga.pay.recargatransactions.application.credit.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreditRequestedEvent(
        UUID id,
        BigDecimal amount,
        int accountNumber,
        int accountAgency,
        OffsetDateTime referenceDate,
        String description) {
}
