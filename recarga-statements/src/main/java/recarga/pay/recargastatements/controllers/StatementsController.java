package recarga.pay.recargastatements.controllers;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recarga.pay.recargastatements.application.GetStatementResponse;
import recarga.pay.recargastatements.domain.StatementRepository;

import java.time.OffsetDateTime;

@RestController
public class StatementsController {

    private final StatementRepository statementRepository;

    public StatementsController(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    @GetMapping("/statements/agency/{agency}/account/{account}")
    public ResponseEntity<?> getStatements(
            @PathVariable int agency,
            @PathVariable int account,
            @Nullable @RequestParam OffsetDateTime startDate,
            @Nullable @RequestParam OffsetDateTime endDate) {

        if (startDate == null)
            startDate = OffsetDateTime.now().minusDays(7);

        if (endDate == null)
            endDate = OffsetDateTime.now();

        if (startDate.compareTo(endDate) == 0){
            var error = new ErrorResponse("DateRange", "Start date must be before end date");
            return ResponseEntity.badRequest().body(error);
        }

        var statementRepository = this.statementRepository.findByAccountAgencyAndAccountNumberAndReferenceDateBetween(
                agency, account, startDate, endDate, Sort.by(Sort.Order.desc("referenceDate")));

        var response = statementRepository.stream().map(x ->
                new GetStatementResponse(
                        x.getTransactionId(),
                        x.getAmount(),
                        x.getAccountNumber(),
                        x.getAccountAgency(),
                        x.getReferenceDate(),
                        x.getDescription(),
                        x.getOperationType(),
                        x.getBalance()
                )).toList();

        return ResponseEntity.ok(response);
    }
}
