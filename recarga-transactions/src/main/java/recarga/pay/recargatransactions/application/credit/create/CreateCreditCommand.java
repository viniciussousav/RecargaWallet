package recarga.pay.recargatransactions.application.credit.create;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateCreditCommand(
        UUID id,
        BigDecimal amount,
        int accountNumber,
        int accountAgency,
        OffsetDateTime referenceDate,
        String description
) {
}
