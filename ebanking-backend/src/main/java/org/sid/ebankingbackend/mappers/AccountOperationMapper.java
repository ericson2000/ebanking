package org.sid.ebankingbackend.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.sid.ebankingbackend.dtos.AccountOperationDto;
import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;

import java.util.Objects;


@Mapper
public interface AccountOperationMapper {

    AccountOperationMapper INSTANCE = Mappers.getMapper(AccountOperationMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "operationDate", target = "operationDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "description", target = "description")
    AccountOperationDto accountOperationToAccountOperationDto(AccountOperation accountOperation);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "operationDate", target = "operationDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "description", target = "description")
    AccountOperation accountOperationDtoToAccountOperation(AccountOperationDto accountOperationDto);

}
