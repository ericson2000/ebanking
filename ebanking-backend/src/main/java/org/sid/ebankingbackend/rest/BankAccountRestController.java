package org.sid.ebankingbackend.rest;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CurrentAccountMapper;
import org.sid.ebankingbackend.mappers.SavingAccountMapper;
import org.sid.ebankingbackend.services.AccountOperationService;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BankAccountRestController {


    private BankAccountService bankAccountService;

    private AccountOperationService<BankAccount> accountOperationService;

    public BankAccountRestController(BankAccountService bankAccountService, AccountOperationService<BankAccount> accountOperationService) {
        this.bankAccountService = bankAccountService;
        this.accountOperationService = accountOperationService;
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return ResponseEntity.ok(bankAccountService.getBankAccount(accountId));
    }

    @GetMapping("/accounts/")
    public ResponseEntity<List<BankAccountDto>> listAccounts() {
        return ResponseEntity.ok(bankAccountService.getListBankAccount());
    }

    @GetMapping("/accounts/{accountId}/operations")
    public ResponseEntity<List<AccountOperationDto>> getHistory(@PathVariable String accountId) {
        return ResponseEntity.ok(accountOperationService.accountHistory(accountId));
    }

    @PostMapping("/accounts/debit/{accountId}/{amount}")
    public ResponseEntity<String> debit(@PathVariable String accountId,
                                        @PathVariable double amount,
                                        @RequestParam(name = "description", defaultValue = "debit") String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        accountOperationService.debit(accountId, amount, description);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/credit/{accountId}/{amount}")
    public ResponseEntity<String> credit(@PathVariable String accountId,
                                         @PathVariable double amount,
                                         @RequestParam(name = "description", defaultValue = "debit") String description) throws BankAccountNotFoundException {
        accountOperationService.credit(accountId, amount, description);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/transfert/{accountIdSource}/{accountIdDestination}/{amount}")
    public ResponseEntity<String> transfert(@PathVariable String accountIdSource,
                                            @PathVariable String accountIdDestination,
                                            @PathVariable double amount
    ) throws BankAccountNotFoundException, BalanceNotSufficientException {
        accountOperationService.transfert(accountIdSource, accountIdDestination, amount);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public ResponseEntity<AccountHistoryDto> getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {

        return ResponseEntity.ok(accountOperationService.getAccountHistory(accountId, page, size));
    }

    @PostMapping(value = "/accounts/ca/")
    public ResponseEntity<BankAccountDto> saveCurrentAccount(@Valid @RequestBody CurrentAccountDto currentAccountDto) throws CustomerNotFoundException {

        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(
                currentAccountDto.getBalance(),
                currentAccountDto.getCustomerDto().getId()
                ,currentAccountDto.getOverDraft());
        return ResponseEntity.ok(CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount));
    }

    @PostMapping(value = "/accounts/sa/")
    public ResponseEntity<BankAccountDto> saveSavingAccount(@Valid @RequestBody SavingAccountDto savingAccountDto) throws CustomerNotFoundException {

        SavingAccount savingAccount = bankAccountService.saveSavingAccount(
                savingAccountDto.getBalance()
                ,savingAccountDto.getCustomerDto().getId()
                ,savingAccountDto.getInterestRate());
        return ResponseEntity.ok(SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount));
    }
}
