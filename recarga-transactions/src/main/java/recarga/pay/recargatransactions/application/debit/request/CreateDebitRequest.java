package recarga.pay.recargatransactions.application.debit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateDebitRequest(
        @NotNull UUID id,
        @Positive BigDecimal amount,
        @Positive int accountNumber,
        @Positive int accountAgency,
        @NotNull OffsetDateTime referenceDate,
        @NotBlank String description
)  {
}