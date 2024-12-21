package recarga.pay.recargabalance.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recarga.pay.recargabalance.domain.Balance;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findBalanceByAccountAgencyAndAccountNumber(int accountAgency, int accountNumber);
}