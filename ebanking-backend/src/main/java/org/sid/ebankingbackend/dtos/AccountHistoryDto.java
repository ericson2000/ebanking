package org.sid.ebankingbackend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountHistoryDto {

    private String accountId;

    private double balance;

    private int currentPage;

    private  int totalPage;

    private  int pageSize;

    List<AccountOperationDto> accountOperationDtos;
}
