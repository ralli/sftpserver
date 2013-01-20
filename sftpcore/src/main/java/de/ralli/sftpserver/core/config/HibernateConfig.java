package de.ralli.sftpserver.core.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import de.ralli.sftpserver.core.entities.Partner;
import de.ralli.sftpserver.core.entities.PartnerKey;
import de.ralli.sftpserver.core.entities.SSHPublicKey;

@Configuration
public class HibernateConfig {
    @Bean
    public FactoryBean<SessionFactory> sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        localSessionFactoryBean.setHibernateProperties(hibernateProperties);
        Class<?>[] classes = { Partner.class, SSHPublicKey.class, PartnerKey.class };
        localSessionFactoryBean.setAnnotatedClasses(classes);
        return localSessionFactoryBean;
    }
}
