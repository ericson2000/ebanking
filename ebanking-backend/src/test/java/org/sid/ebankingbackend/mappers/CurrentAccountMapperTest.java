package org.sid.ebankingbackend.mappers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;

public class CurrentAccountMapperTest {

    @Test
    void given_currentAccount_id_map_currentAccountDto_id(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId("id");

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals(currentAccount.getId(), currentAccountDto.getId());
    }

    @Test
    void given_currentAccount_balance_map_currentAccountDto_balance(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setBalance(2.0);

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals(currentAccount.getBalance(),currentAccountDto.getBalance());
    }

    @Test
    void given_currentAccount_createdAt_map_currentAccountDto_createdAt(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setCreatedAt(new Date());

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals(currentAccount.getBalance(),currentAccountDto.getBalance());
    }

    @Test
    void given_currentAccount_status_map_currentAccountDto_status(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setStatus(AccountStatus.ACTIVATED);

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals(currentAccount.getStatus(),currentAccountDto.getStatus());
    }

    @Test
    void given_currentAccount_customer_map_currentAccountDto_customerDto(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setCustomer(Customer.builder().id(2L).build());

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals(currentAccount.getCustomer().getId(),currentAccountDto.getCustomerDto().getId());
    }

    @Test
    void given_currentAccount_map_savingAccountDto_type_to_CurrentAccount(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals("CurrentAccount",currentAccountDto.getType());
    }

    @Test
    void given_currentAccount_overDraft_map_currentAccountDto_overDraft(){
        //GIVEN
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setOverDraft(3.5);

        //WHEN
        final CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //THEN
        Assertions.assertEquals(currentAccount.getOverDraft(),currentAccountDto.getOverDraft());
    }

    @Test
    void given_currentAccountDto_id_map_currentAccountDto_id(){
        //GIVEN
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        currentAccountDto.setId("id");

        //WHEN
        final CurrentAccount currentAccount = CurrentAccountMapper.INSTANCE.currentAccountDtoToCurrentAccount(currentAccountDto);

        //THEN
        Assertions.assertEquals(currentAccount.getId(), currentAccountDto.getId());
    }

    @Test
    void given_currentAccountDto_balance_map_savingAccount_balance(){
        //GIVEN
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        currentAccountDto.setBalance(2.0);

        //WHEN
        final CurrentAccount currentAccount = CurrentAccountMapper.INSTANCE.currentAccountDtoToCurrentAccount(currentAccountDto);

        //THEN
        Assertions.assertEquals(currentAccount.getBalance(),currentAccountDto.getBalance());
    }

    @Test
    void given_currentAccountDto_createdAt_map_savingAccount_createdAt(){
        //GIVEN
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        currentAccountDto.setCreatedAt(new Date());

        //WHEN
        final CurrentAccount currentAccount = CurrentAccountMapper.INSTANCE.currentAccountDtoToCurrentAccount(currentAccountDto);

        //THEN
        Assertions.assertEquals(currentAccount.getBalance(),currentAccountDto.getBalance());
    }

    @Test
    void given_currentAccountDto_status_map_savingAccount_status(){
        //GIVEN
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        currentAccountDto.setStatus(AccountStatus.ACTIVATED);

        //WHEN
        final CurrentAccount currentAccount = CurrentAccountMapper.INSTANCE.currentAccountDtoToCurrentAccount(currentAccountDto);

        //THEN
        Assertions.assertEquals(currentAccount.getStatus(),currentAccountDto.getStatus());
    }

    @Test
    void given_currentAccountDto_customerDto_map_savingAccount_customer(){
        //GIVEN
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        currentAccountDto.setCustomerDto(CustomerDto.builder().id(2L).build());

        //WHEN
        final CurrentAccount currentAccount = CurrentAccountMapper.INSTANCE.currentAccountDtoToCurrentAccount(currentAccountDto);

        //THEN
        Assertions.assertEquals(currentAccount.getCustomer().getId(),currentAccountDto.getCustomerDto().getId());
    }

    @Test
    void given_currentAccountDto_interestRate_map_savingAccount_interestRate(){
        //GIVEN
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        currentAccountDto.setOverDraft(3.5);

        //WHEN
        final CurrentAccount currentAccount = CurrentAccountMapper.INSTANCE.currentAccountDtoToCurrentAccount(currentAccountDto);

        //THEN
        Assertions.assertEquals(currentAccount.getOverDraft(),currentAccountDto.getOverDraft());
    }
}
