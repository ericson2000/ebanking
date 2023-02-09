package org.sid.ebankingbackend.entities;

import lombok.*;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4,discriminatorType = DiscriminatorType.STRING)
public abstract class BankAccount {

    @Id
    private String id;

    private double balance;

    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;

    public BankAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer,List<AccountOperation> accountOperations){
        this.id = id;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
        this.customer = customer;
        this.accountOperations = accountOperations;
    }
}
