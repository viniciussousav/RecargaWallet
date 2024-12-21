package recarga.pay.recargatransactions.application.debit.success;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DebitSucceededEvent(
        UUID id,
        BigDecimal amount,
        int accountNumber,
        int accountAgency,
        OffsetDateTime referenceDate,
        String description) {

    public String getKey(){
        return String.format("%d#%d", accountAgency, accountNumber);
    }
}