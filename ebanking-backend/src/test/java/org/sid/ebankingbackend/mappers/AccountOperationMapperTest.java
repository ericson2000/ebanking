package org.sid.ebankingbackend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sid.ebankingbackend.dtos.AccountOperationDto;
import org.sid.ebankingbackend.entities.AccountOperation;

import org.sid.ebankingbackend.enums.OperationType;

import java.util.Date;

public class AccountOperationMapperTest {

    @Test
    void given_accountOperation_id_map_accountOperationDto_id(){
        //GIVEN
        AccountOperation accountOperation = AccountOperation.builder().id(1L).build();

        //WHEN
        final AccountOperationDto accountOperationDto = AccountOperationMapper.INSTANCE.accountOperationToAccountOperationDto(accountOperation);

        //THEN
        Assertions.assertEquals(accountOperation.getId(), accountOperationDto.getId());
    }

    @Test
    void given_accountOperation_operationDate_map_accountOperationDto_operationDate(){
        //GIVEN
        AccountOperation accountOperation = AccountOperation.builder().operationDate(new Date()).build();

        //WHEN
        final AccountOperationDto accountOperationDto = AccountOperationMapper.INSTANCE.accountOperationToAccountOperationDto(accountOperation);

        //THEN
        Assertions.assertEquals(accountOperation.getOperationDate(), accountOperationDto.getOperationDate());
    }

    @Test
    void given_accountOperation_amount_map_accountOperationDto_amount(){
        //GIVEN
        AccountOperation accountOperation = AccountOperation.builder().amount(2000.0).build();

        //WHEN
        final AccountOperationDto accountOperationDto = AccountOperationMapper.INSTANCE.accountOperationToAccountOperationDto(accountOperation);

        //THEN
        Assertions.assertEquals(accountOperation.getAmount(), accountOperationDto.getAmount());
    }

    @Test
    void given_accountOperation_type_map_accountOperationDto_type(){
        //GIVEN
        AccountOperation accountOperation = AccountOperation.builder().type(OperationType.DEBIT).build();

        //WHEN
        final AccountOperationDto accountOperationDto = AccountOperationMapper.INSTANCE.accountOperationToAccountOperationDto(accountOperation);

        //THEN
        Assertions.assertEquals(accountOperation.getType(), accountOperationDto.getType());
    }

    @Test
    void given_accountOperation_description_map_accountOperationDto_description(){
        //GIVEN
        AccountOperation accountOperation = AccountOperation.builder().description("description").build();

        //WHEN
        final AccountOperationDto accountOperationDto = AccountOperationMapper.INSTANCE.accountOperationToAccountOperationDto(accountOperation);

        //THEN
        Assertions.assertEquals(accountOperation.getDescription(), accountOperationDto.getDescription());
    }


    @Test
    void given_accountOperationDto_id_map_accountOperation_id(){
        //GIVEN
        AccountOperationDto accountOperationDto = AccountOperationDto.builder().id(1L).build();

        //WHEN
        final AccountOperation accountOperation = AccountOperationMapper.INSTANCE.accountOperationDtoToAccountOperation(accountOperationDto);

        //THEN
        Assertions.assertEquals(accountOperationDto.getId(), accountOperation.getId());
    }

    @Test
    void given_accountOperationDto_operationDate_map_accountOperation_operationDate(){
        //GIVEN
        AccountOperationDto accountOperationDto = AccountOperationDto.builder().operationDate(new Date()).build();

        //WHEN
        final AccountOperation accountOperation = AccountOperationMapper.INSTANCE.accountOperationDtoToAccountOperation(accountOperationDto);

        //THEN
        Assertions.assertEquals(accountOperationDto.getOperationDate(), accountOperation.getOperationDate());
    }

    @Test
    void given_accountOperationDto_amount_map_accountOperation_amount(){
        //GIVEN
        AccountOperationDto accountOperationDto = AccountOperationDto.builder().amount(2000.0).build();

        //WHEN
        final AccountOperation accountOperation = AccountOperationMapper.INSTANCE.accountOperationDtoToAccountOperation(accountOperationDto);

        //THEN
        Assertions.assertEquals(accountOperationDto.getAmount(), accountOperation.getAmount());
    }

    @Test
    void given_accountOperationDto_type_map_accountOperation_type(){
        //GIVEN
        AccountOperationDto accountOperationDto = AccountOperationDto.builder().type(OperationType.DEBIT).build();

        //WHEN
        final AccountOperation accountOperation = AccountOperationMapper.INSTANCE.accountOperationDtoToAccountOperation(accountOperationDto);

        //THEN
        Assertions.assertEquals(accountOperationDto.getType(), accountOperation.getType());
    }

    @Test
    void given_accountOperationDto_description_map_accountOperation_description(){
        //GIVEN
        AccountOperationDto accountOperationDto = AccountOperationDto.builder().description("description").build();

        //WHEN
        final AccountOperation accountOperation = AccountOperationMapper.INSTANCE.accountOperationDtoToAccountOperation(accountOperationDto);

        //THEN
        Assertions.assertEquals(accountOperationDto.getDescription(), accountOperation.getDescription());
    }
}
