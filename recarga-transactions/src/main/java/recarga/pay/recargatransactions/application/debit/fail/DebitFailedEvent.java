package recarga.pay.recargatransactions.application.debit.fail;

import java.math.BigDecimal;
import java.util.UUID;

public record DebitFailedEvent(
        UUID id,
        int agency,
        int account,
        BigDecimal amount,
        String errorCode,
        String errorMessage) {
}