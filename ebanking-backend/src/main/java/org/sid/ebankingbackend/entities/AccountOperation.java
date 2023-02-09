package org.sid.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.OperationType;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AccountOperation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne
    private BankAccount bankAccount;

    private String description;
}
