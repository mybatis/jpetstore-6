package org.mybatis.jpetstore.config;

import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.service.CatalogService;
import org.mybatis.jpetstore.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Igor Baiborodine
 */
@Configuration
@EnableTransactionManagement
public class ServiceConfig {

  @Bean
  public AccountService accountService() {
    return new AccountService();
  }

  @Bean
  public CatalogService catalogService() {
    return new CatalogService();
  }

  @Bean
  public OrderService orderService() {
    return new OrderService();
  }
}
