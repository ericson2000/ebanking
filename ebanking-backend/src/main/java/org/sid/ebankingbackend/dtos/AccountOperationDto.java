package org.sid.ebankingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.enums.OperationType;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperationDto {

    private Long id;

    private Date operationDate;

    private double amount;

    private OperationType type;

    private BankAccountDto bankAccountDto;

    private String description;
}
