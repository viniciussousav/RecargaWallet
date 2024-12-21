package recarga.pay.recargabalance.application;

import jakarta.validation.constraints.Positive;

public record CreateWalletRequest(
        @Positive int agency,
        @Positive int account) {
}
