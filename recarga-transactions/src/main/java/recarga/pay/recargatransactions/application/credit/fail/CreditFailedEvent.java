package recarga.pay.recargatransactions.application.credit.fail;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditFailedEvent(
        UUID id,
        int agency,
        int account,
        BigDecimal amount,
        String errorCode,
        String errorMessage) {
}
