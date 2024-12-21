package recarga.pay.recargastatements.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, Integer> {

    Boolean existsByTransactionIdAndAccountAgencyAndAccountNumber(UUID transactionId, int accountAgency, int accountNumber);

    Statement findTopByAccountAgencyAndAccountNumberOrderByReferenceDateDesc(int accountAgency, int accountNumber);

    List<Statement> findByAccountAgencyAndAccountNumberAndReferenceDateBetween(
            Integer accountAgency,
            Integer accountNumber,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Sort sort);
}
