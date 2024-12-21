package recarga.pay.recargabalance.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "balance")
public class Balance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accountnumber")
    private int accountNumber;

    @Column(name = "accountagency")
    private int accountAgency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "overdraftlimit")
    private BigDecimal overdraftLimit;

    public Long getId() {
        return id;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getAccountAgency() {
        return accountAgency;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    protected Balance() {}

    public Balance(int accountNumber, int accountAgency) {
        this.accountNumber = accountNumber;
        this.accountAgency = accountAgency;
        this.amount = BigDecimal.ZERO;
        this.overdraftLimit = BigDecimal.ZERO;
    }
}
