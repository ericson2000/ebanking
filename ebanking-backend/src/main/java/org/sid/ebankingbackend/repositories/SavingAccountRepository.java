package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.SavingAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingAccountRepository extends BankAccountRepository<SavingAccount> {
}
