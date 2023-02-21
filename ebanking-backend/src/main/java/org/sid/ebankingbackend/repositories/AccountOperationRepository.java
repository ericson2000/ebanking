package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRepository<T extends BankAccount> extends JpaRepository<AccountOperation,Long> {

    List<AccountOperation> findByBankAccountId(String accountId);
}
