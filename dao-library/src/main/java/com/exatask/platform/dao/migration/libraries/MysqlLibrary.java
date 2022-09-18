package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
import com.exatask.platform.utilities.ServiceUtility;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class MysqlLibrary extends AppLibrary {

  private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String CHANGELOG_TABLE = "changelogs";

  public Flyway createRunner(DataSourceProperties dataSourceProperties) {

    DataSource dataSource = prepareMysqlDataSource(dataSourceProperties);
    return createRunner(dataSource);
  }

  public Flyway createRunner(DataSource dataSource) {

    return Flyway.configure()
        .table(CHANGELOG_TABLE)
        .locations("classpath:" + ServiceUtility.getServiceProperty("mysql.changelogs.package"))
        .failOnMissingLocations(true)
        .sqlMigrationSuffixes(".java")
        .validateMigrationNaming(true)
        .installedBy("rohit.aggarwal@exatask.com")
        .dataSource(dataSource)
        .load();
  }

  private static DataSource prepareMysqlDataSource(DataSourceProperties dataSourceProperties) {

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(MYSQL_DRIVER);
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());

    return dataSource;
  }
}