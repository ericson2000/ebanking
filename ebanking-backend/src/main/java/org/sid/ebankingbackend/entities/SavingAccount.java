package org.sid.ebankingbackend.entities;

import lombok.*;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {

    private double interestRate;

    @Builder
    public SavingAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer, List<AccountOperation> accountOperations, double interestRate) {
        super(id, balance, createdAt, status, customer, accountOperations);
        this.interestRate = interestRate;
    }
}
