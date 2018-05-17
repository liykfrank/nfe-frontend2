package org.iata.bsplink.sftpaccountmanager.model.repository;

import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

}
