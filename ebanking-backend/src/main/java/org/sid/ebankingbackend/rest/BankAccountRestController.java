package org.sid.ebankingbackend.rest;

import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountRestController {


    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }
}
