package recarga.pay.recargatransactions.application.transfer.fail;

import java.util.UUID;

public record TransferFailedEvent(UUID id, String errorCode, String errorMessage) {
}
