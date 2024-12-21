package recarga.pay.recargabalance.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import recarga.pay.recargabalance.application.CreateWalletRequest;
import recarga.pay.recargabalance.application.ErrorResponse;
import recarga.pay.recargabalance.application.GetBalanceResponse;
import recarga.pay.recargabalance.domain.Balance;
import recarga.pay.recargabalance.infrastructure.BalanceRepository;

import java.util.Optional;

@RestController
@Validated
public class BalanceController {

    BalanceRepository balanceRepository;

    public BalanceController(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @GetMapping("/balance/agency/{agency}/account/{account}")
    public ResponseEntity<Object> getBalance(
            @Positive @PathVariable(value = "agency") int agency,
            @Positive @PathVariable(value = "account") int account) {
        try {
            Optional<Balance> balance = balanceRepository.findBalanceByAccountAgencyAndAccountNumber(agency, account);

            if (balance.isEmpty()) {
                var error = new ErrorResponse("NotFound", "Account not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            var balanceRow = balance.get();

            var response = new GetBalanceResponse(
                    balanceRow.getAccountNumber(),
                    balanceRow.getAccountAgency(),
                    balanceRow.getAmount(),
                    balanceRow.getOverdraftLimit());

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            var error = new ErrorResponse("InternalServerError", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/balance")
    public ResponseEntity<Object> createWallet(
            @Valid @RequestBody CreateWalletRequest request) {
        try {
            Optional<Balance> balance = balanceRepository.findBalanceByAccountAgencyAndAccountNumber(request.agency(), request.account());

            if (balance.isPresent()) {
                var error = new ErrorResponse("Conflict", "Account already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            var newBalance = new Balance(request.account(), request.agency());
            balanceRepository.save(newBalance);

            var emptyBalance = new GetBalanceResponse(
                    newBalance.getAccountNumber(),
                    newBalance.getAccountAgency(),
                    newBalance.getAmount(),
                    newBalance.getOverdraftLimit());

            return ResponseEntity.status(HttpStatus.CREATED).body(emptyBalance);

        } catch (Exception e) {
            var error = new ErrorResponse("InternalServerError", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
