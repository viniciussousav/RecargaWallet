package recarga.pay.recargatransactions.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recarga.pay.recargatransactions.domain.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findBalanceByAccountAgencyAndAccountNumber(int accountAgency, int accountNumber);
}
