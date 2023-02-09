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
@Builder
@Entity
@Getter
@Setter
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {

    private double overDraft;

    @Builder
    public CurrentAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer, List<AccountOperation> accountOperations, double overDraft) {
        super(id, balance, createdAt, status, customer, accountOperations);
        this.overDraft = overDraft;
    }
}
