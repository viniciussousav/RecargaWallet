package recarga.pay.recargatransactions.application.debit.create;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateDebitCommand(
        UUID id,
        BigDecimal amount,
        int accountNumber,
        int accountAgency,
        OffsetDateTime referenceDate,
        String description
)  {
}