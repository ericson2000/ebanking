package org.sid.ebankingbackend.mappers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.SavingAccount;

import java.util.Date;

public class SavingAccountMapperTest {

    @Test
    void given_savingAccount_id_map_savingAccountDto_id(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId("id");

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals(savingAccount.getId(),savingAccountDto.getId());
    }

    @Test
    void given_savingAccount_balance_map_savingAccountDto_balance(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setBalance(2.0);

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals(savingAccount.getBalance(),savingAccountDto.getBalance());
    }

    @Test
    void given_savingAccount_createdAt_map_savingAccountDto_createdAt(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setCreatedAt(new Date());

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals(savingAccount.getBalance(),savingAccountDto.getBalance());
    }
}
