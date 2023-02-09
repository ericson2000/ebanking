package org.sid.ebankingbackend.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.SavingAccount;

@Mapper
public interface SavingAccountMapper {

    SavingAccountMapper INSTANCE = Mappers.getMapper(SavingAccountMapper.class);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "customerDto", target = "customer")
    @Mapping(source = "interestRate", target = "interestRate")
    SavingAccount savingAccountDtoToSavingAccount(SavingAccountDto savingAccountDto);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "customer", target = "customerDto")
    @Mapping(source = "interestRate", target = "interestRate")
    SavingAccountDto savingAccountToSavingAccountDto(SavingAccount savingAccount);
}
