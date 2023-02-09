package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
