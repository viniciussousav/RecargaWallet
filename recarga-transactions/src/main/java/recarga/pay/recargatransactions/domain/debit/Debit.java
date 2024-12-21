package recarga.pay.recargatransactions.domain.debit;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "debit")
public class Debit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "accountnumber")
    private int accountNumber;

    @Column(name = "accountagency")
    private int AccountAgency;

    @Column(name = "referencedate")
    private OffsetDateTime referenceDate;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DebitStatus status;

    public Debit(UUID id, BigDecimal amount, int accountNumber, int accountAgency, OffsetDateTime referenceDate, String description) {
        this.id = id;
        this.amount = amount;
        this.accountNumber = accountNumber;
        AccountAgency = accountAgency;
        this.referenceDate = referenceDate;
        this.description = description;
        this.status = DebitStatus.REQUESTED;
    }

    protected Debit() {}

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getAccountAgency() {
        return AccountAgency;
    }

    public OffsetDateTime getReferenceDate() {
        return referenceDate;
    }

    public String getDescription() {
        return description;
    }

    public DebitStatus getStatus() {
        return status;
    }

    public void success(){
        this.status = DebitStatus.SUCCEEDED;
    }

    public void fail(){
        this.status = DebitStatus.FAILED;
    }
}
