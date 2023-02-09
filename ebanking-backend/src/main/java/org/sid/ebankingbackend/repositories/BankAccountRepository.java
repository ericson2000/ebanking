package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository<T extends BankAccount> extends JpaRepository<BankAccount,String> {
}
