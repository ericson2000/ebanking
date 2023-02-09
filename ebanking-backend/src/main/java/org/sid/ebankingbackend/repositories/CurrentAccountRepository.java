package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.CurrentAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAccountRepository extends BankAccountRepository<CurrentAccount> {
}
