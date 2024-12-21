package recarga.pay.recargastatements.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreditSucceededEvent(
        UUID id,
        BigDecimal amount,
        int accountNumber,
        int accountAgency,
        OffsetDateTime referenceDate,
        String description) {
}
