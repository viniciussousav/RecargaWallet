package recarga.pay.recargastatements.application;

import recarga.pay.recargastatements.domain.OperationType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GetStatementResponse(
        UUID transactionId,
        BigDecimal amount,
        Integer accountNumber,
        Integer accountAgency,
        OffsetDateTime referenceDate,
        String description,
        OperationType operationType,
        BigDecimal balance) {
}
