package org.sid.ebankingbackend.dtos;

import lombok.*;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;



@NoArgsConstructor
@Getter
@Setter
//@Builder
public abstract class BankAccountDto {

    private String id;

    private double balance;

    private Date createdAt;

    private AccountStatus status;

    private CustomerDto customerDto;

    public BankAccountDto(String id,double balance,Date createdAt,AccountStatus status,CustomerDto customerDto){
        this.id = id;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
        this.customerDto = customerDto;
    }

}
