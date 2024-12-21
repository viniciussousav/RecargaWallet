package recarga.pay.recargatransactions.domain.credit;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit")
public class Credit implements Serializable {

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
    private int accountAgency;

    @Column(name = "referencedate")
    private OffsetDateTime referenceDate;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CreditStatus status;

    public Credit(UUID id, BigDecimal amount, int accountNumber, int accountAgency, OffsetDateTime referenceDate, String description) {
        this.id = id;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.accountAgency = accountAgency;
        this.referenceDate = referenceDate;
        this.description = description;
        this.status = CreditStatus.REQUESTED;
    }

    public Credit() { }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public int getAccountAgency() {
        return this.accountAgency;
    }

    public OffsetDateTime getReferenceDate() {
        return this.referenceDate;
    }

    public String getDescription() {
        return this.description;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void success(){
        this.status = CreditStatus.SUCCEEDED;
    }

    public void fail(){
        this.status = CreditStatus.FAILED;
    }
}

