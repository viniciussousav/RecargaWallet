package recarga.pay.recargabalance.application;

import java.math.BigDecimal;

public record GetBalanceResponse(
        int accountNumber,
        int accountAgency,
        BigDecimal amount,
        BigDecimal overdraftLimit) {
}
