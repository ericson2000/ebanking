package org.sid.ebankingbackend.dtos;

import lombok.*;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;



@NoArgsConstructor
@Getter
@Setter
//public abstract class
public  class BankAccountDto {

    private String id;

    private double balance;

    private Date createdAt;

    private AccountStatus status;

    private CustomerDto customerDto;

    private String type;

    public BankAccountDto(String id,double balance,Date createdAt,AccountStatus status,CustomerDto customerDto,String type){
        this.id = id;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
        this.customerDto = customerDto;
        this.type = type;
    }

}
