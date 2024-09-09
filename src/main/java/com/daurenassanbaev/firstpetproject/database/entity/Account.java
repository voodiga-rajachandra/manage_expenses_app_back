package com.daurenassanbaev.firstpetproject.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account extends AuditingEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "account_name")
    private String accountName;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "currency")
    private String currency;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<RegularPayment> regularPayments;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    private List<AccountTransfer> transfersFrom;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    private List<AccountTransfer> transfersTo;

    public int getId() {
        return this.id;
    }

    public User getOwner() {
        return this.owner;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public List<RegularPayment> getRegularPayments() {
        return this.regularPayments;
    }

    public List<AccountTransfer> getTransfersFrom() {
        return this.transfersFrom;
    }

    public List<AccountTransfer> getTransfersTo() {
        return this.transfersTo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setRegularPayments(List<RegularPayment> regularPayments) {
        this.regularPayments = regularPayments;
    }

    public void setTransfersFrom(List<AccountTransfer> transfersFrom) {
        this.transfersFrom = transfersFrom;
    }

    public void setTransfersTo(List<AccountTransfer> transfersTo) {
        this.transfersTo = transfersTo;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Account)) return false;
        final Account other = (Account) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$owner = this.getOwner();
        final Object other$owner = other.getOwner();
        if (this$owner == null ? other$owner != null : !this$owner.equals(other$owner)) return false;
        final Object this$accountName = this.getAccountName();
        final Object other$accountName = other.getAccountName();
        if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName))
            return false;
        final Object this$transactions = this.getTransactions();
        final Object other$transactions = other.getTransactions();
        if (this$transactions == null ? other$transactions != null : !this$transactions.equals(other$transactions))
            return false;
        final Object this$balance = this.getBalance();
        final Object other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !this$balance.equals(other$balance)) return false;
        final Object this$currency = this.getCurrency();
        final Object other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) return false;
        final Object this$regularPayments = this.getRegularPayments();
        final Object other$regularPayments = other.getRegularPayments();
        if (this$regularPayments == null ? other$regularPayments != null : !this$regularPayments.equals(other$regularPayments))
            return false;
        final Object this$transfersFrom = this.getTransfersFrom();
        final Object other$transfersFrom = other.getTransfersFrom();
        if (this$transfersFrom == null ? other$transfersFrom != null : !this$transfersFrom.equals(other$transfersFrom))
            return false;
        final Object this$transfersTo = this.getTransfersTo();
        final Object other$transfersTo = other.getTransfersTo();
        if (this$transfersTo == null ? other$transfersTo != null : !this$transfersTo.equals(other$transfersTo))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Account;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $owner = this.getOwner();
        result = result * PRIME + ($owner == null ? 43 : $owner.hashCode());
        final Object $accountName = this.getAccountName();
        result = result * PRIME + ($accountName == null ? 43 : $accountName.hashCode());
        final Object $transactions = this.getTransactions();
        result = result * PRIME + ($transactions == null ? 43 : $transactions.hashCode());
        final Object $balance = this.getBalance();
        result = result * PRIME + ($balance == null ? 43 : $balance.hashCode());
        final Object $currency = this.getCurrency();
        result = result * PRIME + ($currency == null ? 43 : $currency.hashCode());
        final Object $regularPayments = this.getRegularPayments();
        result = result * PRIME + ($regularPayments == null ? 43 : $regularPayments.hashCode());
        final Object $transfersFrom = this.getTransfersFrom();
        result = result * PRIME + ($transfersFrom == null ? 43 : $transfersFrom.hashCode());
        final Object $transfersTo = this.getTransfersTo();
        result = result * PRIME + ($transfersTo == null ? 43 : $transfersTo.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", accountName='" + accountName + "', balance=" + balance + ", currency='" + currency + "'}";
    }

}
