package org.sid.ebankingbackend.rest;

import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CurrentAccountMapper;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankAccountRestController {


    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<BankAccountDto> getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return ResponseEntity.ok(bankAccountService.getBankAccount(accountId));
    }

    @GetMapping("/accounts/")
    public ResponseEntity<List<BankAccountDto>> listAccounts() {
        return ResponseEntity.ok(bankAccountService.getListBankAccount());
    }

//    @PostMapping(value = "/accounts", consumes = "application/json", produces = "application/json")
//    public ResponseEntity<BankAccountDto> saveAccount(@RequestBody BankAccountDto bankAccountDto) throws CustomerNotFoundException {
//
//        if (isCurrentAccountDto(bankAccountDto)) {
//            bankAccountService.saveCurrentAccount(bankAccountDto.getBalance(), bankAccountDto.getCustomerDto().getId(), ((CurrentAccountDto) bankAccountDto).getOverDraft());
//        } else if (isSavingAccountDto(bankAccountDto)) {
//            bankAccountService.saveSavingAccount(bankAccountDto.getBalance(), bankAccountDto.getCustomerDto().getId(), ((SavingAccountDto) bankAccountDto).getInterestRate());
//        }
//
//        return null;
//    }

    @PostMapping(value = "/accounts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CurrentAccountDto> saveAccount(@RequestBody CurrentAccountDto currentAccountDto) throws CustomerNotFoundException {

        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(currentAccountDto.getBalance(), currentAccountDto.getCustomerDto().getId(), currentAccountDto.getOverDraft());
        return ResponseEntity.ok(CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount));
    }

    private boolean isCurrentAccountDto(BankAccountDto bankAccountDto) {
        return bankAccountDto instanceof CurrentAccountDto;
    }

    private boolean isSavingAccountDto(BankAccountDto bankAccountDto) {
        return bankAccountDto instanceof SavingAccountDto;
    }
}
