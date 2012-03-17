package org.mybatis.jpetstore.persistence;

import org.mybatis.jpetstore.domain.Account;

public interface AccountMapper {

  Account getAccountByUsername(String username);

  Account getAccountByUsernameAndPassword(String username, String password);

  void insertAccount(Account account);
  
  void insertProfile(Account account);
  
  void insertSignon(Account account);

  void updateAccount(Account account);

  void updateProfile(Account account);

  void updateSignon(Account account);

}
