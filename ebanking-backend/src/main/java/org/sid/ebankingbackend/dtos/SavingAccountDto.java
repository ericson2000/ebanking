package org.sid.ebankingbackend.dtos;


import lombok.*;

import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Builder
public class SavingAccountDto extends BankAccountDto{

    private  double interestRate;

    @Builder
    public SavingAccountDto(String id, double balance, Date createdAt, AccountStatus status, CustomerDto customerDto, double interestRate) {
        super(id, balance, createdAt, status, customerDto);
        this.interestRate = interestRate;
    }
}
