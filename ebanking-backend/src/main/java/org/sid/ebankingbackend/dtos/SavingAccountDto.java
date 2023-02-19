package org.sid.ebankingbackend.dtos;


import lombok.*;

import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavingAccountDto extends BankAccountDto{

    private  double interestRate;

    @Builder
    public SavingAccountDto(String id, double balance, Date createdAt, AccountStatus status, CustomerDto customerDto, double interestRate,String type) {
        super(id, balance, createdAt, status, customerDto,type);
        this.interestRate = interestRate;
    }
}
