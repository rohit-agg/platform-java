package com.exatask.platform.mysql.utilities;

import com.exatask.platform.mysql.AppMysqlTenantConnectionProvider;
import com.exatask.platform.mysql.replicas.ReplicaDataSource;
import com.exatask.platform.mysql.replicas.ReplicaTransactionManager;
import com.exatask.platform.mysql.tenants.MysqlTenantConnections;
import com.exatask.platform.mysql.tenants.MysqlTenantResolver;
import com.exatask.platform.mysql.tenants.ServiceTenant;
import com.exatask.platform.utilities.properties.DataSourceSqlProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.tool.schema.Action;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class TransactionUtility {

  public static PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return new ReplicaTransactionManager(transactionManager);
  }

  public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource, String[] packages) {

    LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(dataSource);
    entityManager.setPackagesToScan(packages);
    entityManager.setJpaVendorAdapter(getVendorAdapter());
    entityManager.setJpaPropertyMap(prepareJpaProperties());

    return entityManager;
  }

  public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(ServiceTenant serviceTenant, String[] packages) {

    MysqlTenantConnections mysqlTenantConnections = new MysqlTenantConnections(serviceTenant);
    MysqlTenantResolver mysqlTenantResolver = new MysqlTenantResolver(serviceTenant);
    AppMysqlTenantConnectionProvider mysqlTenantConnectionProvider = new AppMysqlTenantConnectionProvider(mysqlTenantConnections);

    Map<String, Object> jpaProperties = prepareJpaProperties();
    jpaProperties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE.toString());
    jpaProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, mysqlTenantConnectionProvider);
    jpaProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, mysqlTenantResolver);

    LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(mysqlTenantConnections.getTenantDataSource());
    entityManager.setPackagesToScan(packages);
    entityManager.setJpaVendorAdapter(getVendorAdapter());
    entityManager.setJpaPropertyMap(jpaProperties);

    return entityManager;
  }

  public static DataSource getDataSource(DataSourceSqlProperties dataSourceProperties) {
    return new ReplicaDataSource(dataSourceProperties);
  }

  public static DataSource getDataSource(String url, String username, String password, DataSourceSqlProperties dataSourceProperties) {

    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(url);
    hikariDataSource.setUsername(username);
    hikariDataSource.setPassword(password);

    Optional.ofNullable(dataSourceProperties.getMinimum()).ifPresent(hikariDataSource::setMinimumIdle);
    Optional.ofNullable(dataSourceProperties.getMaximum()).ifPresent(hikariDataSource::setMaximumPoolSize);
    Optional.ofNullable(dataSourceProperties.getTimeout()).ifPresent(hikariDataSource::setConnectionTimeout);
    Optional.ofNullable(dataSourceProperties.getIdleTimeout()).ifPresent(hikariDataSource::setIdleTimeout);

    hikariDataSource.addDataSourceProperty("createDatabaseIfNotExist", true);

    return hikariDataSource;
  }

  private static HibernateJpaVendorAdapter getVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  private static Map<String, Object> prepareJpaProperties() {

    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, Action.NONE.name().toLowerCase());
    jpaProperties.put(AvailableSettings.DIALECT, MySQL8Dialect.class);
    jpaProperties.put(AvailableSettings.JDBC_TIME_ZONE, ZoneOffset.UTC.toString());
    return jpaProperties;
  }
}
