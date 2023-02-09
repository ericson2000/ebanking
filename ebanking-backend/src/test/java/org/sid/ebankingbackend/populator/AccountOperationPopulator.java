package org.sid.ebankingbackend.populator;

import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.enums.OperationType;

import java.util.Date;

public class AccountOperationPopulator {

    public static AccountOperation createAccountOperation(Date operationDate, double amount, OperationType type, BankAccount bankAccount) {
        return AccountOperation.builder().operationDate(operationDate).amount(amount).type(type).bankAccount(bankAccount).build();
    }
}
