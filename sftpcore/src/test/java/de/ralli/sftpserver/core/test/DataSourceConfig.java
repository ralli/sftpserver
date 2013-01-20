package de.ralli.sftpserver.core.test;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        BasicDataSource datasource = new BasicDataSource();
        datasource.setUrl("jdbc:mysql://localhost/sftpconfig");
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUsername("sftpconfig");
        datasource.setPassword("sftpconfig");
        datasource.setInitialSize(5);
        datasource.setMaxActive(10);
        datasource.setMaxIdle(5);
        return datasource;
    }
}
