package org.sid.ebankingbackend.mappers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.enums.AccountStatus;

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

    @Test
    void given_savingAccount_status_map_savingAccountDto_status(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setStatus(AccountStatus.ACTIVATED);

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals(savingAccount.getStatus(),savingAccountDto.getStatus());
    }

    @Test
    void given_savingAccount_customer_map_savingAccountDto_customerDto(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setCustomer(Customer.builder().id(2L).build());

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals(savingAccount.getCustomer().getId(),savingAccountDto.getCustomerDto().getId());
    }

    @Test
    void given_savingAccount_map_savingAccountDto_type_to_SavingAccount(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals("SavingAccount",savingAccountDto.getType());
    }

    @Test
    void given_savingAccount_interestRate_map_savingAccountDto_interestRate(){
        //GIVEN
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setInterestRate(3.5);

        //WHEN
        final SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        //THEN
        Assertions.assertEquals(savingAccount.getInterestRate(),savingAccountDto.getInterestRate());
    }

    @Test
    void given_savingAccountDto_id_map_savingAccountDto_id(){
        //GIVEN
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        savingAccountDto.setId("id");

        //WHEN
        final SavingAccount savingAccount = SavingAccountMapper.INSTANCE.savingAccountDtoToSavingAccount(savingAccountDto);

        //THEN
        Assertions.assertEquals(savingAccount.getId(),savingAccountDto.getId());
    }

    @Test
    void given_savingAccountDto_balance_map_savingAccount_balance(){
        //GIVEN
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        savingAccountDto.setBalance(2.0);

        //WHEN
        final SavingAccount savingAccount = SavingAccountMapper.INSTANCE.savingAccountDtoToSavingAccount(savingAccountDto);

        //THEN
        Assertions.assertEquals(savingAccount.getBalance(),savingAccountDto.getBalance());
    }

    @Test
    void given_savingAccountDto_createdAt_map_savingAccount_createdAt(){
        //GIVEN
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        savingAccountDto.setCreatedAt(new Date());

        //WHEN
        final SavingAccount savingAccount = SavingAccountMapper.INSTANCE.savingAccountDtoToSavingAccount(savingAccountDto);

        //THEN
        Assertions.assertEquals(savingAccount.getBalance(),savingAccountDto.getBalance());
    }

    @Test
    void given_savingAccountDto_status_map_savingAccount_status(){
        //GIVEN
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        savingAccountDto.setStatus(AccountStatus.ACTIVATED);

        //WHEN
        final SavingAccount savingAccount = SavingAccountMapper.INSTANCE.savingAccountDtoToSavingAccount(savingAccountDto);

        //THEN
        Assertions.assertEquals(savingAccount.getStatus(),savingAccountDto.getStatus());
    }

    @Test
    void given_savingAccountDto_customerDto_map_savingAccount_customer(){
        //GIVEN
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        savingAccountDto.setCustomerDto(CustomerDto.builder().id(2L).build());

        //WHEN
        final SavingAccount savingAccount = SavingAccountMapper.INSTANCE.savingAccountDtoToSavingAccount(savingAccountDto);

        //THEN
        Assertions.assertEquals(savingAccount.getCustomer().getId(),savingAccountDto.getCustomerDto().getId());
    }

    @Test
    void given_savingAccountDto_interestRate_map_savingAccount_interestRate(){
        //GIVEN
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        savingAccountDto.setInterestRate(3.5);

        //WHEN
        final SavingAccount savingAccount = SavingAccountMapper.INSTANCE.savingAccountDtoToSavingAccount(savingAccountDto);

        //THEN
        Assertions.assertEquals(savingAccount.getInterestRate(),savingAccountDto.getInterestRate());
    }
}
