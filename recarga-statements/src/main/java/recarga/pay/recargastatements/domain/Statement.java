package recarga.pay.recargastatements.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "Statement")
public class Statement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transactionid")
    private UUID transactionId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "accountnumber")
    private Integer accountNumber;

    @Column(name = "accountagency")
    private Integer accountAgency;

    @Column(name = "referencedate")
    private OffsetDateTime referenceDate;

    @Column(name = "description")
    private String description;

    @Column(name = "operationtype")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "balance")
    private BigDecimal balance;


    protected Statement() {}

    public Statement(UUID transactionId,
                     BigDecimal amount,
                     Integer accountNumber,
                     Integer accountAgency,
                     OffsetDateTime referenceDate,
                     String description,
                     OperationType operationType,
                     BigDecimal balance) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.accountAgency = accountAgency;
        this.referenceDate = referenceDate;
        this.description = description;
        this.operationType = operationType;
        this.balance = balance;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public Integer getAccountAgency() {
        return accountAgency;
    }

    public OffsetDateTime getReferenceDate() {
        return referenceDate;
    }

    public String getDescription() {
        return description;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
