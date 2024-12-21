package recarga.pay.recargatransactions.application.transfer.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTransferRequest(
    @NotNull UUID id,
    @Positive BigDecimal amount,
    @Valid @NotNull Receiver receiver,
    @Valid @NotNull Payer payer,
    @NotNull OffsetDateTime referenceDate
) {
    public record Receiver(
            @Positive int agency,
            @Positive int account,
            @NotBlank String description) { }

    public record Payer(
            @Positive int agency,
            @Positive int account,
            @NotBlank String description) { }
}



