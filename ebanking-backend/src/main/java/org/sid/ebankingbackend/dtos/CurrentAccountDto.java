package org.sid.ebankingbackend.dtos;


import lombok.*;
;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentAccountDto extends BankAccountDto {

    private double overDraft;

    @Builder
    public CurrentAccountDto(String id, double balance, Date createdAt, AccountStatus status, CustomerDto customerDto, double overDraft,String type) {
        super(id, balance, createdAt, status, customerDto,type);
        this.overDraft = overDraft;
    }
}
