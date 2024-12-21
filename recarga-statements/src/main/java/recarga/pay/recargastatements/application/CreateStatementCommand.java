package recarga.pay.recargastatements.application;

import recarga.pay.recargastatements.domain.OperationType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateStatementCommand(
        UUID id,
        BigDecimal amount,
        int agency,
        int account,
        String description,
        OperationType operationType,
        OffsetDateTime referenceDate) {
}
