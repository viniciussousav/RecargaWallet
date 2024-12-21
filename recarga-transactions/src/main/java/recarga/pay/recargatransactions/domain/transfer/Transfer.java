package recarga.pay.recargatransactions.domain.transfer;

import jakarta.persistence.*;
import recarga.pay.recargatransactions.domain.credit.Credit;
import recarga.pay.recargatransactions.domain.debit.Debit;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfer")
public class Transfer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creditid", referencedColumnName = "id")
    private Credit credit;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debitid", referencedColumnName = "id")
    private Debit debit;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "referencedate")
    private OffsetDateTime referenceDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    protected Transfer() { }

    public Transfer(UUID id, BigDecimal amount, Credit credit, Debit debit, OffsetDateTime referenceDate) {
        this.id = id;
        this.amount = amount;
        this.credit = credit;
        this.debit = debit;
        this.referenceDate = referenceDate;
        this.status = TransferStatus.REQUESTED;
    }

    public void fail(){
        this.status = TransferStatus.FAILED;
        this.debit.fail();
        this.credit.fail();
    }

    public void success(){
        this.status = TransferStatus.SUCCEEDED;
        this.debit.success();
        this.credit.success();
    }

    public UUID getId() {
        return id;
    }

    public Credit getCredit() {
        return credit;
    }

    public Debit getDebit() {
        return debit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OffsetDateTime getReferenceDate() {
        return referenceDate;
    }

    public TransferStatus getStatus() {
        return status;
    }
}

