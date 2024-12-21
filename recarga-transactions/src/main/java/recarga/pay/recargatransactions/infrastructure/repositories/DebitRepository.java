package recarga.pay.recargatransactions.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recarga.pay.recargatransactions.domain.debit.Debit;

import java.util.UUID;

@Repository
public interface DebitRepository extends JpaRepository<Debit, UUID> {
}
