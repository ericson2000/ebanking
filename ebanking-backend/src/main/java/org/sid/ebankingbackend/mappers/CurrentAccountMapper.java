package org.sid.ebankingbackend.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;

import java.util.Objects;

@Mapper
public interface CurrentAccountMapper {

    CurrentAccountMapper INSTANCE = Mappers.getMapper(CurrentAccountMapper.class);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "customerDto", target = "customer")
    @Mapping(source = "overDraft", target = "overDraft")
    CurrentAccount currentAccountDtoToCurrentAccount(CurrentAccountDto currentAccountDto);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "customer", target = "customerDto")
    @Mapping(source = "overDraft", target = "overDraft")
    @Mapping(source = "currentAccount", target = "type",qualifiedByName = "toType")

    CurrentAccountDto currentAccountToCurrentAccountDto(CurrentAccount currentAccount);

    @Named("toType")
    static String toType(CurrentAccount currentAccount){
        return Objects.nonNull(currentAccount)?currentAccount.getClass().getSimpleName():null;
    }
}
